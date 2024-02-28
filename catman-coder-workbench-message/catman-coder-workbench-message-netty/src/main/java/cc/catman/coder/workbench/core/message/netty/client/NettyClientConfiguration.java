package cc.catman.coder.workbench.core.message.netty.client;

import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeConfiguration;
import lombok.Data;

@Data
public class NettyClientConfiguration {
    private String host;
    private int port;
    private int bossThreadCount;

    private MessageSerializeConfiguration messageSerializeConfiguration;

    public NettyClientConfiguration() {
        this.host = "localhost";
        this.port = 6666;
        this.bossThreadCount = 1;
        this.messageSerializeConfiguration = new MessageSerializeConfiguration();
    }
}
