package codex.encode;

import codex.serde.kryo.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.minlog.Log;
import de.javakaffee.kryoserializers.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *  This encoder is thread safe
 */
public class KryoEncoder implements Encoder{

    public static final KryoEncoder DEFAULT = new KryoEncoder();

    /**
     * Externally registered Serializer(s) for Classes
     */
    private static final ConcurrentHashMap<Class, Serializer> serializers = new ConcurrentHashMap<>();
    /**
     * Externally registered Classes
     */

    private static final ConcurrentSkipListSet<Class> regClasses = new ConcurrentSkipListSet<>();
    /**
     * Externally registered Registration instances.
     */
    private static final ConcurrentSkipListSet<Registration> regs = new ConcurrentSkipListSet<>();

    /**
     * If any external registered Serializer, Class or Registration this counter is updated
     * which will trigger any Kryo thread locals to register all external registrations.
     */
    private static final AtomicInteger UPDATE_COUNTER = new AtomicInteger(0);


    /**
     * Kryo is not thread safe, so we need to create ThreadLocals for it.
     */
    static private final ThreadLocal<KryosWrapper> KRYOS = new ThreadLocal<KryosWrapper>(){

        @Override
        protected KryosWrapper initialValue() {
            Kryo kryo = createKryoInstance();
            int count = UPDATE_COUNTER.get();

            registerSerializers(kryo);

            return new KryosWrapper(kryo, count);
        }

        @Override
        public KryosWrapper get() {

            // Get the Kryo instance
            KryosWrapper kryo = super.get();

            // check that all external registered classes, serializers and regs have not changed
            int counter = UPDATE_COUNTER.get();

            while(kryo.counter != counter) {
                // exterrnal registrations have changed, re-run register
                registerSerializers(kryo.kryo);

                kryo.counter = counter;
                counter = UPDATE_COUNTER.get();
            }

            return kryo;
        }
    };

    /**
     * Registers all the Serializers and registered classes to the kryo instance.
     * @param kryo
     */
    private static void registerSerializers(Kryo kryo) {

        for(Map.Entry<Class, Serializer> entry : serializers.entrySet()){
            kryo.register(entry.getKey(), entry.getValue());
        }

        for(Class cls : regClasses) {
            kryo.register(cls);
        }


        for(Registration reg : regs) {
            kryo.register(reg);
        }
    }

    private static Kryo createKryoInstance(){

        /**
         * We need to register the most common types so that kryo can deserialize them
         * any extra classes need to use the static register functions
         */
        Kryo kryo = new  KryoReflectionFactorySupport() {

            @Override
            public Serializer<?> getDefaultSerializer(final Class clazz) {
                if ( EnumSet.class.isAssignableFrom( clazz ) ) {
                    return new EnumSetSerializer();
                }
                if ( EnumMap.class.isAssignableFrom( clazz ) ) {
                    return new EnumMapSerializer();
                }
                if ( SubListSerializers.ArrayListSubListSerializer.canSerialize( clazz ) ) {
                    return SubListSerializers.createFor( clazz );
                }

                if ( Date.class.isAssignableFrom( clazz ) ) {
                    return new DateSerializer( clazz );
                }

                return super.getDefaultSerializer(clazz);
            }

        };

        kryo.getWarnUnregisteredClasses();

        kryo.register(ArrayList.class);

        kryo.register(Set.class);
        kryo.register(HashSet.class);

        kryo.register(Map.class, new MapSerializer());
        kryo.register(HashMap.class, new MapSerializer());
        kryo.register(LinkedHashMap.class, new MapSerializer());


        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
        kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
        kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
        kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
        kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
        kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
        kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
        kryo.register(InvocationHandler.class, new JdkProxySerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);

        kryo.register(getMapEntry().getClass(), new MapEntrySerde());
        kryo.register(getMapEntrySet().getClass(), new MapEntrySetSerde());

        kryo.register(Map.Entry.class, new MapEntrySerde());


        kryo.register(BigInteger.class, new BigIntSerde());
        kryo.register(BigDecimal.class, new BigDecSerde());

        kryo.register(byte[].class, new ByteArraySerde());
        kryo.register(int[].class, new IntArraySerde());
        kryo.register(long[].class, new LongArraySerde());
        kryo.register(double[].class, new DoubleArraySerde());
        kryo.register(float[].class, new FloatArraySerde());


        return kryo;
    }

    private static final Map.Entry getMapEntry() {
        return getMapEntrySet().iterator().next();
    }

    private static final Set<Map.Entry> getMapEntrySet() {
        Map m = new HashMap();
        m.put("a", "b");

        return  m.entrySet();
    }

    public static void setInfoLog() {
        com.esotericsoftware.minlog.Log.set(Log.LEVEL_INFO);
    }

    public static void setTraceLog() {
        com.esotericsoftware.minlog.Log.set(Log.LEVEL_TRACE);
    }

    /**
     * Register with the global kryo instance
     * @param clazz
     */
    public static final void register(Class clazz) {
        regClasses.add(clazz);
        UPDATE_COUNTER.incrementAndGet();
    }

    /**
     * Register with the global kryo instance
     * @param registration
     */
    public static final void register(Registration registration) {
        regs.add(registration);
        UPDATE_COUNTER.incrementAndGet();
    }

    /**
     * Register with the global kryo instance
     * @param clazz
     * @param serializer
     */
    public static final void register(Class clazz, Serializer serializer) {
        serializers.put(clazz, serializer);
        UPDATE_COUNTER.incrementAndGet();
    }



    public final <T> T decodeObject(InputStream stream) {
        Input input = new Input(stream);
        return (T) KRYOS.get().kryo.readClassAndObject(input);
    }

    public final <T> T decodeObject(byte[] bts) {
        Input input = new Input(bts);
        return (T) KRYOS.get().kryo.readClassAndObject(input);
    }

    public final <T> T decodeObject(Class<T> clazz, InputStream stream) {
        Input input = new Input(stream);
        return (T) KRYOS.get().kryo.readClassAndObject(input);
    }

    public final <T> T decodeObject(Class<T> clazz, byte[] bts) {
        Input input = new Input(bts);
        return (T) KRYOS.get().kryo.readClassAndObject(input);
    }

    public final <T> byte[] encodeObject(T obj) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        encodeObject(stream, obj);

        return stream.toByteArray();
    }

    public final <T> void  encodeObject(OutputStream stream, T obj) {

        Output output = new Output(stream);
        KRYOS.get().kryo.writeClassAndObject(output, obj);
        output.close();
    }


    private static class KryosWrapper {
        final Kryo kryo;
        int counter;

        public KryosWrapper(Kryo kryo, int counter) {
            this.kryo = kryo;
            this.counter = counter;
        }
    }

}
