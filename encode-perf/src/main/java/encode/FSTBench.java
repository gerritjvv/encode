package encode;

import org.nustaq.serialization.FSTConfiguration;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.Map;

/**
 * Read: https://github.com/RuedigerMoeller/fast-serialization/wiki/Serialization
 */
public class FSTBench extends Base {

    public static final FSTConfiguration FST_CONFIGURATION = FSTConfiguration.createDefaultConfiguration();
    public static final FSTConfiguration FST_FAST_CONFIGURATION = FSTConfiguration.createUnsafeBinaryConfiguration();

    public static final byte[] ENCODED_RAW_OBJ = encodeObject(FST_CONFIGURATION, RAW_OBJECT);
    public static final byte[] ENCODED_FAST_RAW_OBJ = encodeObject(FST_FAST_CONFIGURATION, RAW_OBJECT);


    @Benchmark
    public void encodeRaw() {

        byte[] buff = encodeObject(FST_CONFIGURATION, RAW_OBJECT);

        checkNotNull(buff);
    }

    @Benchmark
    public void decodeRaw() {
        decodeRaw(FST_CONFIGURATION, ENCODED_RAW_OBJ);
    }

    @Benchmark
    public void encodeFastRaw() {

        byte[] buff = encodeObject(FST_FAST_CONFIGURATION, RAW_OBJECT);

        checkNotNull(buff);
    }

    @Benchmark
    public void decodeFastRaw() {
        decodeRaw(FST_FAST_CONFIGURATION, ENCODED_FAST_RAW_OBJ);
    }



    public void decodeRaw(FSTConfiguration conf, byte[] bts) {

        Map<String, Object> data = (Map<String, Object>) conf.asObject(bts);
        checkNotNull(data);
    }


    public static final byte[] encodeObject(FSTConfiguration conf, Map obj) {
        return conf.asByteArray(obj);
    }

}
