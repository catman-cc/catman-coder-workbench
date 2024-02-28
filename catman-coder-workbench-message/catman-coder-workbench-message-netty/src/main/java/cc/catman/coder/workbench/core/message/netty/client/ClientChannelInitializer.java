package cc.catman.coder.workbench.core.message.netty.client;

import cc.catman.coder.workbench.core.message.netty.pipline.MessageDecoder;
import cc.catman.coder.workbench.core.message.netty.pipline.MessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

   private NettyClientConfiguration configuration;

    public ClientChannelInitializer(NettyClientConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new MessageEncoder(configuration.getMessageSerializeConfiguration()))
                .addLast(new MessageDecoder(configuration.getMessageSerializeConfiguration()))
                .addLast(new MessageClientHandler());
        afterInitChannel(socketChannel);
    }

    protected void afterInitChannel(SocketChannel socketChannel) throws Exception {

    }
}
