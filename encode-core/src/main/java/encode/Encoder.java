package encode;

import java.io.InputStream;
import java.io.OutputStream;

public interface Encoder {

    <T> T decodeObject(Class<T> clazz, InputStream stream);
    <T> T decodeObject(Class<T> clazz, byte[] bts);
    <T> byte[] encodeObject(T obj);

    <T> void  encodeObject(OutputStream stream, T obj);

}
