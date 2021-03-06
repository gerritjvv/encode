package codex.encode;

import crypto.Key;
import codex.encode.CryptoEncoder;
import codex.encode.Encoder;
import codex.encode.KryoEncoder;
import codex.encode.Lz4Encoder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Lz4EncoderTest extends BaseTest{

    @Test
    public void testEncodeDecodeLz4() {

        Encoder encoder = Lz4Encoder.getEncoder(KryoEncoder.DEFAULT);

        Map rawObj = buildRawObject();

        byte[] bts = encoder.encodeObject(rawObj);

        System.out.println("Lz4 Size: " + bts.length);

        Map decodedObj = encoder.decodeObject(HashMap.class, bts);

        assertEquals(rawObj.get("id"), decodedObj.get("id"));
    }

    @Test
    public void testEncodeDecodeLz4CBCHmac() {

        Key.ExpandedKey key = Key.KeySize.AES_256.genKeysHmacSha();

        Encoder encoder = CryptoEncoder.getCBCHmacInstance(key, Lz4Encoder.getEncoder(KryoEncoder.DEFAULT));

        Map rawObj = buildRawObject();

        byte[] bts = encoder.encodeObject(rawObj);

        System.out.println("AES CBC HMAC Lz4 Size: " + bts.length);

        Map decodedObj = encoder.decodeObject(HashMap.class, bts);

        assertEquals(rawObj.get("id"), decodedObj.get("id"));
    }
}
