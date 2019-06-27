package codex.serde.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecSerde extends Serializer<BigDecimal> {

    @Override
    public void write(Kryo kryo, Output output, BigDecimal b) {


        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(output);
            outputStream.writeObject(b);

        } catch (IOException e) {
            RuntimeException rte = new RuntimeException(e);
            rte.setStackTrace(e.getStackTrace());
            throw rte;
        }

    }

    @Override
    public BigDecimal read(Kryo kryo, Input input, Class type) {
          try {

              ObjectInputStream inputStream = new ObjectInputStream(input);
              return (BigDecimal) inputStream.readObject();
          } catch (Exception e) {
            RuntimeException rte = new RuntimeException(e);
            rte.setStackTrace(e.getStackTrace());
            throw rte;
        }
    }

    @Override
    public boolean isImmutable() {
        return true;
    }
}
