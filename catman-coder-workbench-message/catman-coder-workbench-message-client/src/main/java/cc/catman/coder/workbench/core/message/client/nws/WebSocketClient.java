package cc.catman.coder.workbench.core.message.client.nws;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.client.WebsocketMessageConnection;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
public final class WebSocketClient {

    private URI uri;
    private String scheme;
    private String host;
    private int port;
    private SslContext sslCtx;

    private WebSocketClientConfiguration configuration;

    private Channel channel;

    private WebSocketClientHandler handler;

    private final CountDownLatch countDownLatch=new CountDownLatch(1);

    public static WebSocketClient of(WebSocketClientConfiguration configuration) {
        return new WebSocketClient(configuration);
    }

    public WebSocketClient(WebSocketClientConfiguration configuration) {
        this.configuration=configuration;
        processorUrl(configuration.getUrl());
    }

    @SneakyThrows
    public WebSocketClient start() {
        this.doStart(true);
        return this;
    }

    public WebSocketClient startAsync(){
        this.doStart(false);
        return this;
    }

    @SneakyThrows
    private Channel doStart(boolean sync) {
        EventLoopGroup group = new NioEventLoopGroup();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
        WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(
                this.uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        this.handler = new WebSocketClientHandler(webSocketClientHandshaker,this.configuration.getTextMessageHandler());

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        if (sslCtx != null) {
                            p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                        }
                        p.addLast(
                                new HttpClientCodec(),
                                new HttpObjectAggregator(8192),
                                WebSocketClientCompressionHandler.INSTANCE,
                                handler);
                    }
                });
        ChannelFuture connect = b.connect(uri.getHost(), port);
        connect.addListener(future -> {
            if (future.isSuccess()) {
               // 注册一个系统通道
                this.initSystemChannel(connect.channel());
            }
        });
        if (sync){
            Channel ch = connect.sync().channel();
            this.handler.handshakeFuture().sync();
            this.channel=ch;
            return ch;
        }
        Channel ch = connect.channel();
        this.channel = ch;
        return ch;
    }

    public void initSystemChannel(Channel channel){
        IMessageExchange exchange = this.configuration.getMessageExchange();
        ChannelManager cm = exchange.getChannelManager();
        // 首次创建链接,为链接创建一个默认的信道,该信道用于处理默认数据,比如心跳,系统消息等
        MessageConnection<?> connection = getOrCreateConnection(cm,channel);
        CreateChannelOptions options = CreateChannelOptions.builder()
                .channelId("default")
                .kind("default")
                .build();


        log.info("create default channel for connection {}", connection.getId());
        cm.create(options, connection);
        // 系统启动时,尝试建立默认系统通道
        this.send(Message.of(options),channel);
    }

    private MessageConnection<?> getOrCreateConnection( ChannelManager cm,Channel channel) {
        return cm.getMessageConnectionManager()
                .getOrCreateConnection(
                        channel.id().asLongText(),
                        id -> WebsocketMessageConnection.builder()
                                .id(id)
                                .rawConnection(this)
                                .build());
    }

    public void await(){
        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
        }
    }

    @SneakyThrows
    public MessageACK send(Message<?> message){
       return this.send(message,this.channel);
    }

    @SneakyThrows
    protected MessageACK send(Message<?> message, Channel channel){
        ObjectMapper objectMapper = this.configuration.getObjectMapper();
        String msg = objectMapper.writeValueAsString(message);
        TextWebSocketFrame textMsg = new TextWebSocketFrame(msg);
        channel.writeAndFlush(textMsg);
        return MessageACK.ACK;
    }
    public Channel channel() {
        return this.channel;
    }


    @SneakyThrows
    public void processorUrl(String url) {
        this.uri = URI.create(url);
        this.scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        this.host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                this.port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                this.port = 443;
            } else {
                this.port = -1;
            }
        } else {
            this.port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) is supported.");
            return;
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }
    }
}
