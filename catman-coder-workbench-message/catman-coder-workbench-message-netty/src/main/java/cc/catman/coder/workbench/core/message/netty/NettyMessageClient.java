package cc.catman.coder.workbench.core.message.netty;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.netty.client.ClientChannelInitializer;
import cc.catman.coder.workbench.core.message.netty.client.NettyClientConfiguration;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty消息客户端,用于连接NettyMessageServer提供的服务
 */
public class NettyMessageClient {

    private Channel channel;

   private NettyClientConfiguration configuration;

    public NettyMessageClient(NettyClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public static NettyMessageClient create(NettyClientConfiguration configuration) {
        return new NettyMessageClient(configuration);
    }

    public Channel connect() {
        // 创建netty客户端链接

     try {
         Bootstrap bootstrap = new Bootstrap();
         ChannelFuture future =  bootstrap.group(new NioEventLoopGroup())
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 .handler(new ClientChannelInitializer(configuration))
                 .connect(configuration.getHost(), configuration.getPort());

         future.addListener(f -> {
             if (f.isSuccess()) {
                 System.out.println("连接成功");
             } else {
                 System.out.println("连接失败");
             }
         });
            // 阻塞等待连接成功
            future.sync();
            // 获取channel
            channel = future.channel();
            channel.closeFuture().addListener(f -> {
                System.out.println("连接关闭");
            });
            return channel;
     } catch (InterruptedException e) {
         throw new RuntimeException(e);
     } finally {
            // 释放资源
            //group.shutdownGracefully();
     }
    }

    public Channel getConnection() {
        if (channel==null){
           return connect();
        }
        if (channel.isActive()){
            return channel;
        }
        return connect();
    }

    public void send(Message<?> message) {
        Channel channel = getConnection();
        // 将数据发送到服务端
        // 在发送到服务端之前,根据需要考虑添加一些额外的处理逻辑
        // 比如,添加系统级监听器,监听消息发送的状态
        channel.writeAndFlush(message);
    }
}
