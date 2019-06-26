package encode;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import de.javakaffee.kryoserializers.*;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.util.*;

public class KryoBench extends Base {

    private static final Kryo KRYO;
    public static final byte[] ENCODED_RAW_OBJ;

    static {

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


        ENCODED_RAW_OBJ = encodeObject(KRYO, RAW_OBJECT);


    }


    @Benchmark
    public void encodeRaw() {

        byte[] buff = encodeObject(KRYO, RAW_OBJECT);

        checkNotNull(buff);
    }

    @Benchmark
    public void decodeRaw() {
        Input input = new Input(ENCODED_RAW_OBJ);

        Map<String, Object> data = KRYO.readObject(input, HashMap.class);

        checkNotNull(data);
    }


    public static final byte[] encodeObject(Kryo kryo, Map obj) {

//        printObject(obj);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Output output = new Output(stream);
        kryo.writeObject(output, obj);
        output.close();

        return stream.toByteArray();
    }


    private static void printObject(Object obj) {

        if(obj instanceof Map) {
            Map m = (Map)obj;
            System.out.println("------ MAP ------ " + m.getClass());

            for (Object k : m.keySet()) {
                Object v = m.get(k);

                System.out.println("Key: " + k + " obj : ");
                printObject(v);
            }

            System.out.println("------ END MAP ------");

        } else if (obj instanceof List) {
            List l = (List)obj;
            System.out.println("------ LIST ------ " + l.getClass());
            l.forEach(KryoBench::printObject);
            System.out.println("------ END LIST ------");
        } else {
            System.out.println("Plain OBJ: " + obj.getClass());
        }
    }
}
