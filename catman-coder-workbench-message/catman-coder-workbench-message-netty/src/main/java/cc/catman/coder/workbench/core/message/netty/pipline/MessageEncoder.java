package cc.catman.coder.workbench.core.message.netty.pipline;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerialize;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeConfiguration;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Optional;
@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<Message> {
    private MessageSerializeConfiguration messageSerializeConfiguration;

    public MessageEncoder(MessageSerializeConfiguration messageSerializeConfiguration) {
        this.messageSerializeConfiguration = messageSerializeConfiguration;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        byte serializeType = messageSerializeConfiguration.getDefaultSerializeType();
        Optional<MessageSerialize> serialize = messageSerializeConfiguration.getMessageSerializeFactory().getSerialize(serializeType);
        if (serialize.isEmpty()) {
            throw new IllegalArgumentException("serialize type " + serializeType + " not found");
        }
        byte[] bytes = serialize.get().encode(msg);
        byte[] serializeTypeBytes = new byte[bytes.length+1];
        serializeTypeBytes[0] = serializeType;
        System.arraycopy(bytes,0,serializeTypeBytes,1,bytes.length);

        ByteBufAllocator alloc = ctx.alloc();
        ByteBuf buffer = alloc.buffer();
        buffer.writeBytes(serializeTypeBytes);
        out.add(buffer);
    }
}
