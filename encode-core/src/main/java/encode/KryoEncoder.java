package encode;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import de.javakaffee.kryoserializers.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.util.*;

public class KryoEncoder implements Encoder{

    public static final KryoEncoder DEFAULT = new KryoEncoder();

    private static final Kryo KRYO;

    static {
        /**
         * We need to register the most common types so that kryo can deserialize them
         * any extra classes need to use the static register functions
         */

        KRYO = new  KryoReflectionFactorySupport() {

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

        KRYO.getWarnUnregisteredClasses();

        Kryo kryo = KRYO;

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

    }


    /**
     * Register with the global kryo instance
     * @param clazz
     */
    public static final void register(Class clazz) {
        KRYO.register(clazz);
    }

    /**
     * Register with the global kryo instance
     * @param registration
     */
    public static final void register(Registration registration) {
        KRYO.register(registration);
    }

    /**
     * Register with the global kryo instance
     * @param clazz
     * @param serializer
     */
    public static final void register(Class clazz, Serializer serializer) {
        KRYO.register(clazz, serializer);
    }


    public final <T> T decodeObject(Class<T> clazz, InputStream stream) {
        Input input = new Input(stream);
        return KRYO.readObject(input, clazz);
    }

    public final <T> T decodeObject(Class<T> clazz, byte[] bts) {
        Input input = new Input(bts);
        return KRYO.readObject(input, clazz);
    }

    public final <T> byte[] encodeObject(T obj) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        encodeObject(stream, obj);

        return stream.toByteArray();
    }

    public final <T> void  encodeObject(OutputStream stream, T obj) {

        Output output = new Output(stream);
        KRYO.writeObject(output, obj);
        output.close();
    }


}
