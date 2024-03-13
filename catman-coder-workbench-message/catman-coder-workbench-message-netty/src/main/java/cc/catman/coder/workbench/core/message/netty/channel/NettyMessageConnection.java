package cc.catman.coder.workbench.core.message.netty.channel;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.MessageContext;
import cc.catman.coder.workbench.core.message.connection.AbstractMessageConnection;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于Netty的消息连接
 */
@Slf4j
public class NettyMessageConnection extends AbstractMessageConnection<ChannelHandlerContext> {

    public static final String PREFIX="netty-";
    @Override
    public boolean isAlive() {
        return this.getRawConnection().channel().isActive();
    }

    @Override
    public void close() {
        ChannelHandlerContext rawConnection = this.getRawConnection();
        rawConnection.close().addListener(future -> {
            if (future.isSuccess()) {
                // 关闭成功
                log.info("close connection {} success", this.getId());
            } else {
                // 关闭失败
                log.error("close connection {} fail", this.getId());
            }
        });
    }

    @Override
    public MessageContext getContext() {
        return null;
    }

    @Override
    public MessageACK send(Message message) {
        ChannelFuture channelFuture = this.getRawConnection().writeAndFlush(message);
        return MessageACK.ACK;
    }

    public static NettyMessageConnection create(ChannelHandlerContext channelHandlerContext) {
        NettyMessageConnection connection = new NettyMessageConnection();
        connection.setRawConnection(channelHandlerContext);
        connection.setId(channelHandlerContext.channel().id().asLongText());
        connection.setType("netty");
        return connection;
    }
}
