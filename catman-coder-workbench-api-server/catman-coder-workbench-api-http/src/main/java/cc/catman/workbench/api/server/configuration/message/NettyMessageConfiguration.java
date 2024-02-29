package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import cc.catman.coder.workbench.core.message.netty.NettyMessageServer;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeConfiguration;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeFactory;
import cc.catman.coder.workbench.core.message.netty.server.NettyMessageServerConfiguration;
import cc.catman.workbench.api.server.configuration.message.handlers.NettyMessageChannelHandler;
import cc.catman.workbench.api.server.configuration.message.serialize.FuryMessageSerialize;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

@Slf4j
@Configuration
public class NettyMessageConfiguration  {

    @Resource
    private DefaultMessageExchange exchange;

    @Resource
    private ChannelManager channelManager;

    @PostConstruct
    public void afterPropertiesSet() throws Exception {

        NettyMessageServerConfiguration configuration=new NettyMessageServerConfiguration();
        MessageSerializeConfiguration messageSerializeConfiguration = configuration.getMessageSerializeConfiguration();
        messageSerializeConfiguration.setSerializeType(FuryMessageSerialize.FURY_SERIALIZE_TYPE);
        MessageSerializeFactory messageSerializeFactory = messageSerializeConfiguration.getMessageSerializeFactory();

        messageSerializeFactory.register(FuryMessageSerialize.FURY_SERIALIZE_TYPE, new FuryMessageSerialize());
        configuration.addChannelHandler(new NettyMessageChannelHandler(exchange,channelManager));

        NettyMessageServer server=NettyMessageServer.create(configuration);
        new Thread(server::start).start();
    }
}
