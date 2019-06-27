package codex.encode;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class KryoEncoderTest extends BaseTest{

    @Test
    public void testEncodeDecode() {

        Map rawObj = buildRawObject();

        byte[] bts = KryoEncoder.DEFAULT.encodeObject(rawObj);


        Map decodedObj = KryoEncoder.DEFAULT.decodeObject(HashMap.class, bts);

        assertEquals(rawObj.get("id"), decodedObj.get("id"));
    }

    @Test
    public void testBigDecimalEncodeDecode() {

        BigDecimal big = new BigDecimal("9999999999999999999999999.99999999999999999999999");

        byte[] bts = KryoEncoder.DEFAULT.encodeObject(big);


        BigDecimal big2 = KryoEncoder.DEFAULT.decodeObject(BigDecimal.class, bts);

        assertEquals(big, big2);
    }

    @Test
    public void testBigIntegerEncodeDecode() {

        BigInteger big = new BigInteger("999999999999999999999999999999999999999999999999");

        byte[] bts = KryoEncoder.DEFAULT.encodeObject(big);


        BigInteger big2 = KryoEncoder.DEFAULT.decodeObject(BigInteger.class, bts);

        assertEquals(big, big2);
    }
}
