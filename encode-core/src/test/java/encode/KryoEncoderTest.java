package encode;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
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
}
