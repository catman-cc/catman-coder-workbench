package cc.catman.coder.workbench.core.message.netty;

import cc.catman.coder.workbench.core.message.netty.server.NettyMessageServerConfiguration;
import cc.catman.coder.workbench.core.message.netty.server.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyMessageServer {

    private volatile boolean isRunning = false;

    private  EventLoopGroup bossGroup;
    private  EventLoopGroup workerGroup;

    private NettyMessageServerConfiguration configuration;

    private NettyMessageServer() {
        this.configuration = new NettyMessageServerConfiguration();
        this.bossGroup = new NioEventLoopGroup(configuration.getBossThreadCount());
        this.workerGroup = new NioEventLoopGroup(configuration.getWorkerThreadCount());
    }

    public static NettyMessageServer create(NettyMessageServerConfiguration configuration) {
        NettyMessageServer server = new NettyMessageServer();
        server.bossGroup = new NioEventLoopGroup(configuration.getBossThreadCount());
        server.workerGroup = new NioEventLoopGroup(configuration.getWorkerThreadCount());
        server.configuration = configuration;
        return server;
    }

    public void start() {
        if (isRunning) {
            log.warn("MessageServer[NETTY] is already running.");
            return;
        }
        isRunning = true;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer(configuration))
            ;

           bootstrap.bind(configuration.getPort()).addListener(f -> {
                if (f.isSuccess()) {
                    log.info("MessageServer[NETTY] start success,on port:{}", configuration.getPort());
                    this.registryShutdownHook();
                } else {
                    log.error("MessageServer[NETTY] start failed.", f.cause());
                }
            }).channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("MessageServer[NETTY] start failed.", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (!isRunning) {
            log.warn("NettyMessageServer is not running.");
            return;
        }
        isRunning = false;
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    protected void registryShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }
}
