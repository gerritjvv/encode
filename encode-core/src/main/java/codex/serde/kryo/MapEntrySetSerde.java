package codex.serde.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapEntrySetSerde extends Serializer<Set<Map.Entry>> {

    @Override
    public void write(Kryo kryo, Output output, Set<Map.Entry> e) {
        int count = e.size();
        output.writeInt(count);

        for(Map.Entry entry : e) {
            kryo.writeClassAndObject(output, entry.getKey());
            kryo.writeClassAndObject(output, entry.getValue());
        }
    }

    @Override
    public Set<Map.Entry> read(Kryo kryo, Input input, Class type) {
        int count = input.readInt();

        Set<Map.Entry> set = new HashSet<>();

        for(int i = 0; i < count; i++) {
            Map.Entry entry = new MapEntrySerde.MapEntry(
                    kryo.readClassAndObject(input),
                    kryo.readClassAndObject(input)
                    );
            set.add(entry);

        }

        return set;
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
