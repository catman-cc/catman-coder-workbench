package cc.catman.workbench.api.server.configuration.netty;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.scheduling.annotation.Async;

import java.net.InetSocketAddress;
@Slf4j
@Configuration
public class NettyHttpTriggerConfiguration {

    @Async
    @PostConstruct
    public void startNettyServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Netty server shutdown");
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }));

        bootstrap.group(boss,work)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer());

        ChannelFuture f = bootstrap.bind(new InetSocketAddress(9999));
        f.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口绑定成功!");
            } else {
                System.out.println("端口绑定失败!");
            }
        }).sync();
    }

}
