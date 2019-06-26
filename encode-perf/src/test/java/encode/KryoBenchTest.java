package encode;

import org.junit.Test;

public class KryoBenchTest {

    private final KryoBench bench = new KryoBench();


    @Test
    public void testEncode(){
        //test no exception
        bench.encodeRaw();

        System.out.println("Encode Default Size: " + KryoBench.ENCODED_RAW_OBJ.length);
    }

    @Test
    public void testDecode() {
        //test no exception
        bench.decodeRaw();
    }
}
