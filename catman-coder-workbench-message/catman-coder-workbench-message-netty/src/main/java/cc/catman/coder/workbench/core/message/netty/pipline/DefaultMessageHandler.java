package cc.catman.coder.workbench.core.message.netty.pipline;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *  默认的消息处理器
 */
public class DefaultMessageHandler extends SimpleChannelInboundHandler<Message<?>> {
    private IMessageExchange messageExchange;


    public DefaultMessageHandler(IMessageExchange messageExchange) {
        this.messageExchange = messageExchange;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<?> message) throws Exception {
    }
}
