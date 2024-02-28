package cc.catman.coder.workbench.core.message.netty.server;

import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeConfiguration;
import io.netty.channel.ChannelInboundHandler;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NettyMessageServerConfiguration {
    /**
     * 服务端口
     */
    private int port;
    /**
     * boss线程数,默认为1,用于处理连接,接收请求
     */
    private int bossThreadCount;
    /**
     * worker线程数,默认为CPU核数*2,用于处理IO操作
     */
    private int workerThreadCount;

    private List<ChannelInboundHandler> channelInboundHandlers;

    private MessageSerializeConfiguration messageSerializeConfiguration;

    public NettyMessageServerConfiguration() {
        this.port = 6666;
        this.bossThreadCount = 1;
        this.workerThreadCount = Runtime.getRuntime().availableProcessors() * 2;
        this.messageSerializeConfiguration = new MessageSerializeConfiguration();
        this.channelInboundHandlers=new ArrayList<>();
    }

    public void addChannelHandler(ChannelInboundHandler channelInboundHandler){
        this.channelInboundHandlers.add(channelInboundHandler);
    }
}
