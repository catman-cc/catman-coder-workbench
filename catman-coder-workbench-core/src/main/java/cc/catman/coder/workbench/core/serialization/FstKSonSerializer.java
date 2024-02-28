package cc.catman.coder.workbench.core.serialization;

import org.nustaq.serialization.FSTConfiguration;

public class FstKSonSerializer implements ICatManSerialization{
    private  FSTConfiguration conf;
    public FstKSonSerializer() {
       conf = FSTConfiguration.createDefaultConfiguration();
        conf.setCrossPlatform(true);
    }
    @Override
    public byte[] serialize(Object obj) {
        return conf.asByteArray(obj);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return conf.asObject(bytes);
    }
}
