package encode;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Base {

    public static final Map<String, Object> RAW_OBJECT = buildRawObject();


    private static final Map<String, Object> buildRawObject() {
        return readFile("src/resources/raw_object.json");
    }


    private static final Map<String, Object> readFile(String filename) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filename), HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkNotNull(Object data) {
        if (data == null) {
            throw new RuntimeException("Object cannot be null here");
        }
    }
}
