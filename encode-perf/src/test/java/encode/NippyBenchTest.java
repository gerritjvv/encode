package encode;

import org.junit.Test;

public class NippyBenchTest {

    private final NippyBench bench = new NippyBench();

    @Test
    public void testEncode(){
        //test no exception
        bench.encodeRaw();
        System.out.println("Encode Default Size: " + NippyBench.ENCODED_RAW_OBJ.length);

    }

    @Test
    public void testDecode() {
        //test no exception
        bench.decodeRaw();

    }
}
