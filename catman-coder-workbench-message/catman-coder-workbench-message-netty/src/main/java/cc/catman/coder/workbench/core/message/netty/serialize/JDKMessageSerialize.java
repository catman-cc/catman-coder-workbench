package cc.catman.coder.workbench.core.message.netty.serialize;

import cc.catman.coder.workbench.core.message.Message;
import org.springframework.util.SerializationUtils;

public class JDKMessageSerialize implements MessageSerialize{
    public static final byte JDK_SERIALIZE = 1;

    @Override
    public Message<?> decode(byte[] bytes) {
        Object d = SerializationUtils.deserialize(bytes);
        if (d instanceof Message) {
            return (Message<?>) d;
        }
        // 序列化失败,返回null
        return null;
    }

    @Override
    public byte[] encode(Message<?> message) {
        return SerializationUtils.serialize(message);
    }
}
