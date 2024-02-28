package cc.catman.coder.workbench.core.serialization;

import org.nustaq.serialization.FSTConfiguration;

public class FstMinBinSerializer implements ICatManSerialization{
    private  FSTConfiguration conf;
    public FstMinBinSerializer() {
       conf = FSTConfiguration.createMinBinConfiguration();
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

    public static void main(String[] args) {
        FstMinBinSerializer fstMinBinSerializer = new FstMinBinSerializer();
        byte[] serialize = fstMinBinSerializer.serialize("hello");
        System.out.println(serialize);
        System.out.println(fstMinBinSerializer.deserialize(serialize));
    }
}
