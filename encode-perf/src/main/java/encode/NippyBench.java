package encode;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.nustaq.serialization.FSTConfiguration;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.Map;

/**
 * Read: https://github.com/RuedigerMoeller/fast-serialization/wiki/Serialization
 */
public class NippyBench extends Base {

    private static final IFn NIPPY_FREEZE;
    private static final IFn NIPPY_THAW;
    public static final byte[] ENCODED_RAW_OBJ;

    static {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("taoensso.nippy"));

        /**
         call with SECURE_RANDOM to avoid overhead of creating the SecureRandom instance on each invocation.
         see: https://github.com/funcool/buddy-core/blob/master/src/buddy/core/nonce.clj#L34
         and https://github.com/gerritjvv/crypto/issues/1
         */
        NIPPY_FREEZE = Clojure.var("taoensso.nippy", "freeze");
        NIPPY_THAW = Clojure.var("taoensso.nippy", "thaw");

        ENCODED_RAW_OBJ = encodeObject(RAW_OBJECT);
    }

    @Benchmark
    public void encodeRaw() {

        byte[] buff = encodeObject(RAW_OBJECT);

        checkNotNull(buff);
    }

    @Benchmark
    public void decodeRaw() {

        Map<String, Object> data = (Map<String, Object>) NIPPY_THAW.invoke(ENCODED_RAW_OBJ);
        checkNotNull(data);
    }


    public static final byte[] encodeObject(Map obj) {
        return (byte[]) NIPPY_FREEZE.invoke(obj);
    }

}
