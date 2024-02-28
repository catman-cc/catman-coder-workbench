package cc.catman.coder.workbench.core.message.netty.serialize;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultMessageSerializeFactory implements MessageSerializeFactory{

    private static final DefaultMessageSerializeFactory INSTANCE = new DefaultMessageSerializeFactory();

    public static DefaultMessageSerializeFactory getInstance() {
        return INSTANCE;
    }

    private Map<Byte,MessageSerialize> serializes=new HashMap<>();
    public DefaultMessageSerializeFactory() {
       this.register(JDKMessageSerialize.JDK_SERIALIZE,new JDKMessageSerialize());
    }


    @Override
    public Optional<MessageSerialize> getSerialize(byte serializeType) {
        if (serializes.containsKey(serializeType)) {
            return Optional.of(serializes.get(serializeType));
        }
        return Optional.empty();
    }

    @Override
    public void register(byte type, MessageSerialize serialize) {
        if (serializes.containsKey(type)) {
            throw new IllegalArgumentException("serialize type " + type + " already exists");
        }
        serializes.put(type, serialize);
    }
}
