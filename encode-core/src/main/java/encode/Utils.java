package encode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Utils {

    // MAX_SKIP_BUFFER_SIZE is used to determine the maximum buffer size to
    // use when skipping.
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Copied from the Java 1.9 openjdk implementation.
     * @param input
     * @return
     * @throws IOException
     */
    public static final byte[] readAllBytes(InputStream input) throws IOException {
        //copied from java 1.9 InputStream readAllBytes

        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        int capacity = buf.length;
        int nread = 0;
        int n;
        for (;;) {
            // read to EOF which may read more or less than initial buffer size
            while ((n = input.read(buf, nread, capacity - nread)) > 0)
                nread += n;

            // if the last call to read returned -1, then we're done
            if (n < 0)
                break;

            // need to allocate a larger buffer
            if (capacity <= MAX_BUFFER_SIZE - capacity) {
                capacity = capacity << 1;
            } else {
                if (capacity == MAX_BUFFER_SIZE)
                    throw new OutOfMemoryError("Required array size too large");
                capacity = MAX_BUFFER_SIZE;
            }
            buf = Arrays.copyOf(buf, capacity);
        }

        return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
    }

    /**
     * Wraps the Exception in a RuntimeException, setting the stack trace correctly and re-throws it.
     * @param e
     * @return Object we always throw an exception here
     */
    public static final <T> T rethrow(Exception e) {
        RuntimeException rte = new RuntimeException(e);
        rte.setStackTrace(e.getStackTrace());
        throw rte;
    }

}
