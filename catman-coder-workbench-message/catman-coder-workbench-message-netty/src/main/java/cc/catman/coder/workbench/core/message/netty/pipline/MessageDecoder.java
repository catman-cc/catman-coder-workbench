package cc.catman.coder.workbench.core.message.netty.pipline;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerialize;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeConfiguration;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
@ChannelHandler.Sharable
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    private MessageSerializeConfiguration messageSerializeConfiguration;

    public MessageDecoder(MessageSerializeConfiguration messageSerializeConfiguration) {
        this.messageSerializeConfiguration = messageSerializeConfiguration;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readableBytes() < 1) {
            return;
        }
        msg.markReaderIndex();
        // 读取消息的序列化方式
        byte serializeType = msg.readByte();
        // 获取消息序列化方式
        MessageSerializeFactory messageSerializeFactory = messageSerializeConfiguration.getMessageSerializeFactory();
        MessageSerialize serialize = messageSerializeFactory.getSerialize(serializeType)
                .orElseThrow(() -> new IllegalArgumentException("serialize type " + serializeType + " not found"));

        byte[] bytes = new byte[msg.readableBytes()];

        ByteBuf byteBuf = msg.readBytes(msg.readableBytes());
        byteBuf.readBytes(bytes);
        Message<?> message = serialize.decode(bytes);
        // TODO 此处需要填充消息通道信息
        out.add(message);
    }
}
