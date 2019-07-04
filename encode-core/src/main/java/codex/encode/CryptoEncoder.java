package codex.encode;

import crypto.AES;
import crypto.Key;

import java.io.InputStream;
import java.io.OutputStream;

import static codex.encode.Utils.rethrow;

/**
 * An encoder that wraps another Encoder and encrypts/decrypts the encoded data.
 * <p>
 * Supports: GCM and CBC+HMac encryption.
 */
public class CryptoEncoder implements Encoder {


    private final IFn<byte[], byte[]> decryptFn;
    private final IFn<byte[], byte[]> encryptFn;

    private final Encoder encoder;

    private CryptoEncoder(Encoder encoder, IFn<byte[], byte[]> decryptFn, IFn<byte[], byte[]> encryptFn) {
        this.encoder = encoder;
        this.decryptFn = decryptFn;
        this.encryptFn = encryptFn;
    }

    @Override
    public <T> T decodeObject(Class<T> clazz, InputStream stream) {

        try {
            final byte[] bts = Utils.readAllBytes(stream);
            return encoder.decodeObject(clazz, decryptFn.apply(bts));

        } catch (Exception e) {
            return rethrow(e);
        }


    }

    @Override
    public <T> T decodeObject(Class<T> clazz, byte[] bts) {
        try {
            return encoder.decodeObject(clazz, decryptFn.apply(bts));
        } catch (Exception e) {
            return rethrow(e);
        }
    }

    @Override
    public <T> T decodeObject(InputStream stream) {

        try {
            final byte[] bts = Utils.readAllBytes(stream);
            return encoder.decodeObject(decryptFn.apply(bts));

        } catch (Exception e) {
            return rethrow(e);
        }


    }

    @Override
    public <T> T decodeObject(byte[] bts) {
        try {
            return encoder.decodeObject(decryptFn.apply(bts));
        } catch (Exception e) {
            return rethrow(e);
        }
    }

    @Override
    public <T> byte[] encodeObject(T obj) {
        try {
            return encryptFn.apply(encoder.encodeObject(obj));
        } catch (Exception e) {
            return rethrow(e);
        }
    }

    @Override
    public <T> void encodeObject(OutputStream stream, T obj) {
        try {
            final byte[] bts = encryptFn.apply(encoder.encodeObject(obj));
            stream.write(bts);
        } catch (Exception e) {
            rethrow(e);
        }
    }

    /**
     * Returns an encoder that will encryptGCM(encode(...))
     *
     * @param key     The key to use for encryption
     * @param encoder The encoder to use before encryption is applied
     * @return CryptoEncoder
     */
    public static CryptoEncoder getGCMInstance(final Key.ExpandedKey key, final Encoder encoder) {
        return getGCMInstance(0, key, encoder);
    }

    /**
     * Returns an encoder that will encryptGCM(encode(...))
     *
     * @param version  for custom versioning, can be 0 as default
     * @param key      The key to use for encryption
     * @param encoder  The encoder to use before encryption is applied
     * @return CryptoEncoder
     */
    public static CryptoEncoder getGCMInstance(final int version, final Key.ExpandedKey key, final Encoder encoder) {
        return getGCMInstance(version, "SunJCE", key, encoder);
    }


    /**
     * Returns an encoder that will encryptGCM(encode(...))
     *
     * @param version  for custom versioning, can be 0 as default
     * @param provider JCE provider
     * @param key      The key to use for encryption
     * @param encoder  The encoder to use before encryption is applied
     * @return CryptoEncoder
     */
    public static CryptoEncoder getGCMInstance(final int version, final String provider, final Key.ExpandedKey key, final Encoder encoder) {
        final byte v = (byte) version;

        return new CryptoEncoder(
                encoder,
                (byte[] input) -> AES.decryptGCM(v, provider, key, input),
                (byte[] input) -> AES.encryptGCM(v, provider, key, input));
    }

    /**
     * Returns an encoder that will encryptCBCHmac(encode(...))
     * Which hmac is used i.e 256, 512 depends on the key.
     *
     * @param key     The key to use for encryption
     * @param encoder The encoder to use before encryption is applied
     * @return CryptoEncoder
     */
    public static CryptoEncoder getCBCHmacInstance(final Key.ExpandedKey key, final Encoder encoder) {
        return getCBCHmacInstance(0, key, encoder);
    }

    /**
     * Returns an encoder that will encryptCBCHmac(encode(...))
     * Which hmac is used i.e 256, 512 depends on the key.
     *
     * @param version for custom versioning, can be 0 as default
     * @param key     The key to use for encryption
     * @param encoder The encoder to use before encryption is applied
     * @return CryptoEncoder
     */
    public static CryptoEncoder getCBCHmacInstance(final int version, final Key.ExpandedKey key, final Encoder encoder) {
        return getCBCHmacInstance(version, "SunJCE", key, encoder);
    }

    /**
     * Returns an encoder that will encryptCBCHmac(encode(...))
     * Which hmac is used i.e 256, 512 depends on the key.
     *
     * @param version  for custom versioning, can be 0 as default
     * @param provider JCE provider
     * @param key      The key to use for encryption
     * @param encoder  The encoder to use before encryption is applied
     * @return CryptoEncoder
     */
    public static CryptoEncoder getCBCHmacInstance(final int version, final String provider, final Key.ExpandedKey key, final Encoder encoder) {
        final byte v = (byte) version;

        return new CryptoEncoder(
                encoder,
                (byte[] input) -> AES.decryptCBC(v, provider, key, input),
                (byte[] input) -> AES.encryptCBC(v, provider, key, input));
    }

}
