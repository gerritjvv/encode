package encode;

import crypto.Key;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EncodeBench extends Base {

    public static final byte[] ENCODED_RAW_OBJ_LZ4GCM;
    public static final byte[] ENCODED_RAW_OBJ_GCM;
    public static final byte[] ENCODED_RAW_OBJ_LZ4;

    public static final Encoder ENCODER_LZ4GCM;
    public static final Encoder ENCODER_GCM;
    public static final Encoder ENCODER_LZ4;



    static {
        Key.ExpandedKey key = Key.KeySize.AES_128.genKeysHmacSha();
        ENCODER_LZ4GCM = CryptoEncoder.getGCMInstance(key, Lz4Encoder.getEncoder(KryoEncoder.DEFAULT));
        ENCODER_GCM = CryptoEncoder.getGCMInstance(key, KryoEncoder.DEFAULT);
        ENCODER_LZ4 = Lz4Encoder.getEncoder(KryoEncoder.DEFAULT);


        ENCODED_RAW_OBJ_LZ4GCM = encodeObject(ENCODER_LZ4GCM, RAW_OBJECT);
        ENCODED_RAW_OBJ_GCM = encodeObject(ENCODER_GCM, RAW_OBJECT);
        ENCODED_RAW_OBJ_LZ4 = encodeObject(ENCODER_LZ4, RAW_OBJECT);


    }

    @Benchmark
    public void encodeLz4AesGCMRaw() {

        byte[] buff = encodeObject(ENCODER_LZ4GCM, RAW_OBJECT);

        checkNotNull(buff);
    }

    @Benchmark
    public void decodeLz4AesGCMRaw() {
        decodeRaw(ENCODER_LZ4GCM, ENCODED_RAW_OBJ_LZ4GCM);
    }


    @Benchmark
    public void encodeAesGCMRaw() {

        byte[] buff = encodeObject(ENCODER_GCM, RAW_OBJECT);

        checkNotNull(buff);
    }


    @Benchmark
    public void decodeAesGCMRaw() {
        decodeRaw(ENCODER_GCM, ENCODED_RAW_OBJ_GCM);
    }

    @Benchmark
    public void decodeLz4Raw() {
        decodeRaw(ENCODER_LZ4, ENCODED_RAW_OBJ_LZ4);
    }



    @Benchmark
    public void encodeLz4Raw() {

        byte[] buff = encodeObject(ENCODER_LZ4, RAW_OBJECT);

        checkNotNull(buff);
    }



    public void decodeRaw(Encoder encoder, byte[] bts) {

        Map<String, Object> data = encoder.decodeObject(HashMap.class, bts);
        checkNotNull(data);
    }


    public static final byte[] encodeObject(Encoder encoder, Map obj) {
        return encoder.encodeObject(obj);
    }

}
