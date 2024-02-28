package cc.catman.coder.workbench.core.message.netty.client;

import cc.catman.coder.workbench.core.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageClientHandler extends SimpleChannelInboundHandler<Message<?>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message<?> message) throws Exception {
        // 接收到消息之后需要填充消息通道
        System.out.println("收到消息: " + message);
    }
}
