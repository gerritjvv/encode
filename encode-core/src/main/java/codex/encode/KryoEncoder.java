package codex.encode;

import codex.serde.kryo.BigDecSerde;
import codex.serde.kryo.BigIntSerde;
import codex.serde.kryo.MapEntrySerde;
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

/**
 *
 */
public class KryoEncoder implements Encoder{

    public static final KryoEncoder DEFAULT = new KryoEncoder();

    static private final ThreadLocal<Kryo> kryos = ThreadLocal.withInitial(() -> createKryoInstance());

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

        kryo.register(getMapEntry(), new MapEntrySerde());

        kryo.register(Map.Entry.class, new MapEntrySerde());

        kryo.register(BigInteger.class, new BigIntSerde());
        kryo.register(BigDecimal.class, new BigDecSerde());

        return kryo;
    }

    private static final Class getMapEntry() {
        Map m = new HashMap();
        m.put("a", "b");

        Set<Map.Entry> entrySet = m.entrySet();
        return entrySet.iterator().next().getClass();
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
        kryos.get().register(clazz);
    }

    /**
     * Register with the global kryo instance
     * @param registration
     */
    public static final void register(Registration registration) {
        kryos.get().register(registration);
    }

    /**
     * Register with the global kryo instance
     * @param clazz
     * @param serializer
     */
    public static final void register(Class clazz, Serializer serializer) {
        kryos.get().register(clazz, serializer);
    }



    public final <T> T decodeObject(InputStream stream) {
        Input input = new Input(stream);
        return (T) kryos.get().readClassAndObject(input);
    }

    public final <T> T decodeObject(byte[] bts) {
        Input input = new Input(bts);
        return (T) kryos.get().readClassAndObject(input);
    }

    public final <T> T decodeObject(Class<T> clazz, InputStream stream) {
        Input input = new Input(stream);
        return (T) kryos.get().readClassAndObject(input);
    }

    public final <T> T decodeObject(Class<T> clazz, byte[] bts) {
        Input input = new Input(bts);
        return (T) kryos.get().readClassAndObject(input);
    }

    public final <T> byte[] encodeObject(T obj) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        encodeObject(stream, obj);

        return stream.toByteArray();
    }

    public final <T> void  encodeObject(OutputStream stream, T obj) {

        Output output = new Output(stream);
        kryos.get().writeClassAndObject(output, obj);
        output.close();
    }


}
