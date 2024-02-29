package cc.catman.workbench.api.server.configuration.message.handlers;

import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.MessageConnectionManager;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.netty.channel.NettyMessageConnection;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class NettyMessageChannelHandler extends SimpleChannelInboundHandler<Message<?>> {
    private final IMessageExchange exchange;

    private final ChannelManager channelManager;

    public NettyMessageChannelHandler(IMessageExchange exchange, ChannelManager channelManager) {
        this.exchange = exchange;
        this.channelManager = channelManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<?> message) throws Exception {
        MessageConnectionManager messageConnectionManager = this.channelManager.getMessageConnectionManager();

       channelManager.getOrCreateChannel(message, () ->
                messageConnectionManager.getOrCreateConnection(
                        NettyMessageConnection.PREFIX + channelHandlerContext.channel().id().asLongText()
                        , (id) -> NettyMessageConnection.create(channelHandlerContext)
                )
        );

        exchange.exchange(message);
    }
}
