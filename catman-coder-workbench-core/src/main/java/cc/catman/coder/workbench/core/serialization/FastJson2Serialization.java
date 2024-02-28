package cc.catman.coder.workbench.core.serialization;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FastJson2Serialization implements ICatManSerialization{
    static {
        ObjectReader<Object> objectReader = new ObjectReader<>() {
            @Override
            public Object readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
                return null;
            }
        };
        JSON.register(Object.class, objectReader);
        JSON.register(Map.class,objectReader);
        JSON.register(HashMap.class,objectReader);
    }
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj, JSONWriter.Feature.ReferenceDetection);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return JSON.parseObject(bytes);
    }
}
