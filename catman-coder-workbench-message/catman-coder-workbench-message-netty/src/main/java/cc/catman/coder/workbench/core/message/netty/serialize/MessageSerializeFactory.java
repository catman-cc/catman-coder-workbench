package cc.catman.coder.workbench.core.message.netty.serialize;

import java.util.Optional;

/**
 * 消息序列化工厂,用于获取消息序列化方式
 * <p>
 *    为了支持多种序列化方式,每一种序列化方式都有一个长度为8bit的标识,用于标识消息的序列化方式,因此,最多支持256种序列化方式
 */
public interface MessageSerializeFactory {
    Optional<MessageSerialize> getSerialize(byte serializeType);

    void register(byte type, MessageSerialize serialize);
}
