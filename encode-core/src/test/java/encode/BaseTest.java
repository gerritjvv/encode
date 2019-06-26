package encode;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    protected static final Map<String, Object> buildRawObject() {
        return readFile("src/test/resources/raw_object.json");
    }


    protected static final Map<String, Object> readFile(String filename) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filename), HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
