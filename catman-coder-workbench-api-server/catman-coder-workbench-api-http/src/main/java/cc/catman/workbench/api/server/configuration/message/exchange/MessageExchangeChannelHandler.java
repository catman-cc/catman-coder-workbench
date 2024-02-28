package cc.catman.workbench.api.server.configuration.message.exchange;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class MessageExchangeChannelHandler extends SimpleChannelInboundHandler<Message<?>> {
    private IMessageExchange exchange;

    public MessageExchangeChannelHandler(IMessageExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<?> message) throws Exception {
        exchange.exchange(message);
    }
}
