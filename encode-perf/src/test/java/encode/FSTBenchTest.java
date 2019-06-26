package encode;

import org.junit.Test;

public class FSTBenchTest {

    private final FSTBench bench = new FSTBench();


    @Test
    public void testEncodeFast(){
        //test no exception
        bench.encodeFastRaw();

        System.out.println("Encode Fast Size: " + FSTBench.ENCODED_FAST_RAW_OBJ.length);
    }

    @Test
    public void testDecodeFast() {
        //test no exception
        bench.decodeFastRaw();

    }

    @Test
    public void testEncode(){
        //test no exception
        bench.encodeRaw();
        System.out.println("Encode Default Size: " + FSTBench.ENCODED_RAW_OBJ.length);

    }

    @Test
    public void testDecode() {
        //test no exception
        bench.decodeRaw();

    }
}
