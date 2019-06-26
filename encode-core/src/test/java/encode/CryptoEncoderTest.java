package encode;

import crypto.Key;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CryptoEncoderTest extends BaseTest{

    @Test
    public void testEncodeDecodeCBCHmac() {

        Key.ExpandedKey key = Key.KeySize.AES_256.genKeysHmacSha();

        Encoder encoder = CryptoEncoder.getCBCHmacInstance(key, KryoEncoder.DEFAULT);

        Map rawObj = buildRawObject();

        byte[] bts = encoder.encodeObject(rawObj);

        System.out.println("AES CBC Kryo Size: " + bts.length);

        Map decodedObj = encoder.decodeObject(HashMap.class, bts);

        assertEquals(rawObj.get("id"), decodedObj.get("id"));
    }


    @Test
    public void testEncodeDecodeGCM() {

        Key.ExpandedKey key = Key.KeySize.AES_128.genKeysHmacSha();

        Encoder encoder = CryptoEncoder.getGCMInstance(key, KryoEncoder.DEFAULT);

        Map rawObj = buildRawObject();

        byte[] bts = encoder.encodeObject(rawObj);

        System.out.println("AES GCM Kryo Size: " + bts.length);

        Map decodedObj = encoder.decodeObject(HashMap.class, bts);

        assertEquals(rawObj.get("id"), decodedObj.get("id"));
    }

}
