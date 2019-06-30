package codex.encode;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KryoEncoderTest extends BaseTest{

    @Test
    public void testEncodeDecode() {

        Map rawObj = buildRawObject();

        byte[] bts = KryoEncoder.DEFAULT.encodeObject(rawObj);


        Map decodedObj = KryoEncoder.DEFAULT.decodeObject(HashMap.class, bts);

        assertEquals(rawObj.get("id"), decodedObj.get("id"));
    }

    @Test
    public void testMapEntryEncodeDecode() {

        Map rawObj = buildRawObject();

        byte[] bts = KryoEncoder.DEFAULT.encodeObject(rawObj.entrySet());


        Set<Map.Entry> decodedObj = KryoEncoder.DEFAULT.decodeObject(Set.class, bts);

        Map obj2 = new HashMap();

        for(Map.Entry entry : decodedObj) {
            obj2.put(entry.getKey(), entry.getValue());
        }

        assertEquals(rawObj.get("id"), obj2.get("id"));
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

    @Test
    public void testArrayTypesEncodeDecode() {

        byte[] arr = new byte[]{(byte)1, (byte)2};
        byte[] bts = KryoEncoder.DEFAULT.encodeObject(arr);
        byte[] arr2 = KryoEncoder.DEFAULT.decodeObject(bts);
        assertArrayEquals(arr, arr2);

        int[] arrInt = new int[]{1,2,3,4};
        bts = KryoEncoder.DEFAULT.encodeObject(arrInt);
        int[]  arrInt2 = KryoEncoder.DEFAULT.decodeObject(bts);
        assertArrayEquals(arrInt, arrInt2);


        long[] arrLong = new long[]{1L, 2L};
        bts = KryoEncoder.DEFAULT.encodeObject(arrLong);
        long[] arrLong2 = KryoEncoder.DEFAULT.decodeObject(bts);
        assertArrayEquals(arrLong, arrLong2);

        double[] arrDouble = new double[]{0.2D, 100.45D};
        bts = KryoEncoder.DEFAULT.encodeObject(arrDouble);
        double[] arrDouble2 = KryoEncoder.DEFAULT.decodeObject(bts);
        assertTrue(Arrays.equals(arrDouble, arrDouble2));

        float[] arrFloat = new float[]{0.2f, 100.45f};
        bts = KryoEncoder.DEFAULT.encodeObject(arrFloat);
        float[] arrFloat2 = KryoEncoder.DEFAULT.decodeObject(bts);
        assertTrue(Arrays.equals(arrFloat, arrFloat2));


    }
}
