package cc.catman.coder.workbench.core.message.netty.server;

import cc.catman.coder.workbench.core.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageServerHandler extends SimpleChannelInboundHandler<Message<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<?> message) throws Exception {
        System.out.println("receive message: " + message.getPayload());
        channelHandlerContext.writeAndFlush(message);
    }
}
