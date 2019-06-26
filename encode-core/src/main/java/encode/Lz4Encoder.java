package encode;

import net.jpountz.lz4.*;

import java.io.InputStream;
import java.io.OutputStream;

import static encode.Utils.rethrow;

/**
 * An encoder that wraps another Encoder and compress/decompress the encoded data.
 */
public class Lz4Encoder implements Encoder {


    private static final LZ4Factory FACTORY = LZ4Factory.fastestInstance();

    private final Encoder encoder;


    private Lz4Encoder(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public <T> T decodeObject(Class<T> clazz, InputStream stream) {
        try {
            return decodeObject(clazz, Utils.readAllBytes(stream));
        } catch (Exception e) {
            return rethrow(e);
        }
    }

    @Override
    public <T> T decodeObject(Class<T> clazz, byte[] bts) {
        try {

            return encoder.decodeObject(clazz,
                    new LZ4DecompressorWithLength(FACTORY.fastDecompressor()).decompress(bts));

        } catch (Exception e) {
            return rethrow(e);
        }
    }

    @Override
    public <T> byte[] encodeObject(T obj) {
        try {
            return new LZ4CompressorWithLength(
                    FACTORY.fastCompressor()).compress(encoder.encodeObject(obj));
        } catch (Exception e) {
            return rethrow(e);
        }
    }

    @Override
    public <T> void encodeObject(OutputStream stream, T obj) {
        try {
            stream.write(encodeObject(obj));
        } catch (Exception e) {
            rethrow(e);
        }
    }

    /**
     * @param encoder The encoder to use before compression is applied
     * @return Lz4Encoder
     */
    public static Lz4Encoder getEncoder(final Encoder encoder) {
        return new Lz4Encoder(encoder);
    }

}
