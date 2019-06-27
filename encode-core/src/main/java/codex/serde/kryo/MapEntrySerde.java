package codex.serde.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Map;

public class MapEntrySerde extends Serializer<Map.Entry> {

    @Override
    public void write(Kryo kryo, Output output, Map.Entry e) {
        kryo.writeClassAndObject(output, e.getKey());
        kryo.writeClassAndObject(output, e.getValue());
    }

    @Override
    public Map.Entry read(Kryo kryo, Input input, Class type) {
        Object k  = kryo.readClassAndObject(input);
        Object v  = kryo.readClassAndObject(input);

        return new MapEntry(k, v);
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    public static class MapEntry implements Map.Entry {

        final Object k;
        Object v;

        public MapEntry(Object k, Object v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public Object getKey() {
            return k;
        }

        @Override
        public Object getValue() {
            return v;
        }

        @Override
        public Object setValue(Object value) {
            Object oldV = v;
            v = value;
            return  oldV;
        }
    }
}
