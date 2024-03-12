package cc.catman.coder.workbench.core.serialization;

import org.apache.fury.Fury;


/**
 * Fury序列化,性能最好,但是目前处于实验阶段
 */
public class FurySerialization implements ICatManSerialization{
    private Fury fury;

    public FurySerialization(Fury fury) {
        this.fury = fury;
    }

    @Override
    public byte[] serialize(Object obj)  {
       return fury.serialize(obj);
    }

    @Override
    public Object deserialize(byte[] bytes)  {
        return  fury.deserialize(bytes);
    }
}
