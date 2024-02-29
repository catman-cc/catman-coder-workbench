package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.DefaultMessageConnectionManager;
import cc.catman.coder.workbench.core.message.MessageConnectionManager;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelFactory;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelManager;
import cc.catman.coder.workbench.core.message.channel.DefaultMessageChannel;
import cc.catman.coder.workbench.core.message.channel.HttpValueProviderExecutorMessageChannel;
import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import cc.catman.coder.workbench.core.message.match.AntPathMessageMatch;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MessageConfiguration {

    @Bean
    public MessageConnectionManager messageConnectionManager() {
        return DefaultMessageConnectionManager.builder().build();
    }

    @Bean
    public ChannelManager channelManager() {
        DefaultChannelFactory channelFactory = DefaultChannelFactory.builder()
                .build()
                .add("default", (message, connection, channelManager) -> DefaultMessageChannel.builder()
                        .id(message.getChannelId())
                        .connection(connection)
                        .channelManager(channelManager)
                        .build()
                )
                .add("RunSimpleHttpValueProvider", (message, connection, channelManager) -> HttpValueProviderExecutorMessageChannel.builder()
                        .id(message.getChannelId())
                        .connection(connection)
                        .channelManager(channelManager)
                        .build()
                );

        return DefaultChannelManager.builder()
                .channelFactory(channelFactory)
                .messageConnectionManager(messageConnectionManager())
                .build();
    }

    @Bean
    public DefaultMessageExchange exchange() {
        DefaultMessageExchange exchange=new DefaultMessageExchange();

        IMessageSubscriberManager subscriberManager = exchange.getSubscriberManager();

        subscriberManager.addException((message, e) -> {
            log.error("message exchange error", e);
        });

        // no match subscriber
        subscriberManager.addNoMatch((m)->true,(message) -> {
            log.info("netty message:{}",message);
            return new MessageResult();
        });

        // filter
        exchange.add((message -> {
            log.info("netty message:{}",message);
            return true;
        }));

        // 注册节点接入的消息交换策略
        exchange.add(AntPathMessageMatch.of("catman.cc/core/node/**"),(message -> {
            // 处理节点接入信息,这里只是打印消息
            log.info("node-join [1] message:{}",message);
            return new MessageResult();
        }));

        exchange.add(AntPathMessageMatch.of("catman.cc/core/node/**"),(message -> {
            // 处理节点接入信息,这里只是打印消息
            log.info("node-join [2] message:{}",message);
            return new MessageResult();
        }));
        return exchange;
    }

}
