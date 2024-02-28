package cc.catman.coder.workbench.core.message.netty.server;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.netty.pipline.MessageDecoder;
import cc.catman.coder.workbench.core.message.netty.pipline.MessageEncoder;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);

    public ServerChannelInitializer() throws InterruptedException {
    }

    private NettyMessageServerConfiguration configuration;

    public ServerChannelInitializer(NettyMessageServerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline()
                .addLast(new MessageDecoder(configuration.getMessageSerializeConfiguration()))
                .addLast(new MessageEncoder(configuration.getMessageSerializeConfiguration()))
                ;
        configuration.getChannelInboundHandlers().forEach(pipeline::addLast);

        pipeline
                .addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));

        afterInitChannel(socketChannel);
    }

    protected void afterInitChannel(SocketChannel socketChannel) throws Exception {

    }
}