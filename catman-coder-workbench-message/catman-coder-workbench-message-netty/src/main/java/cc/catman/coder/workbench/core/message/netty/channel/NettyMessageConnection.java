package cc.catman.coder.workbench.core.message.netty.channel;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.MessageContext;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

/**
 * 基于Netty的消息连接
 */
public class NettyMessageConnection implements MessageConnection<ChannelHandlerContext> {
    private ChannelHandlerContext channelHandlerContext;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public ChannelHandlerContext getRawConnection() {
        return this.channelHandlerContext;
    }

    @Override
    public boolean isAlive() {
        return channelHandlerContext.channel().isActive();
    }

    @Override
    public MessageContext getContext() {
        return null;
    }

    @Override
    public MessageACK send(Message message) {
        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(message);
        return null;
    }

    public static NettyMessageConnection create(ChannelHandlerContext channelHandlerContext) {
        NettyMessageConnection connection = new NettyMessageConnection();
        connection.channelHandlerContext = channelHandlerContext;
        return connection;
    }
}
