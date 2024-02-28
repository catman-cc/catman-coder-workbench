package cc.catman.coder.workbench.core.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson2.filter.ExtraProcessor;

import java.util.Arrays;

public class FastJsonSerialization implements ICatManSerialization{
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        ParserConfig parserConfig = ParserConfig.global;
        return JSON.parseObject(bytes, Object.class, new SerializeFilter() {
        });
    }
}
