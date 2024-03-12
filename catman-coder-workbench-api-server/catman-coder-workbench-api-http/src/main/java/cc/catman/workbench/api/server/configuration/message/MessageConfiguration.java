package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelFactory;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelManager;
import cc.catman.coder.workbench.core.message.channel.DefaultMessageChannel;
import cc.catman.coder.workbench.core.message.channel.HttpValueProviderExecutorMessageChannel;
import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import cc.catman.coder.workbench.core.message.match.AntPathMessageMatch;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilterFactory;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.subscriber.filter.MessageTypeMessageSubscriberFilterCreator;
import cc.catman.coder.workbench.core.message.subscriber.filter.P2PMessageSubscriberFilterCreator;
import cc.catman.coder.workbench.core.message.system.CreateChannel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class MessageConfiguration {


    @Resource
    private List<IMessageSubscriber> subscribers;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private IdGenerator idGenerator;

    private static MessageChannel createChannel(CreateChannel option, MessageConnection<?> connection, ChannelManager channelManager) {
        String cid = "default-" + connection.getId();
        synchronized (cid) {
            return channelManager.getChannel(cid).orElseGet(() -> DefaultMessageChannel.builder()
                    .id(cid)
                    .connection(connection)
                    .channelManager(channelManager)
                    .build());
        }
    }

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
        DefaultMessageExchange exchange = new DefaultMessageExchange();
        processSubscriberFilters(exchange);
        // 注册JSON对象解码器
        processMessageDecoderFactory(exchange);
        // 注册订阅者
        processSubscriber(exchange);
        processChannelFactory(exchange);

        // 注册自定义订阅者
        subscribers.forEach(exchange::add);
        return exchange;
    }

    private void processChannelFactory(DefaultMessageExchange exchange) {
        // 默认信道,用于处理默认消息,一条连接有且只有一个默认信道
        exchange.getChannelManager().getChannelFactory()
                .add("default", MessageConfiguration::createChannel
                )
                .add("RunSimpleHttpValueProvider", (message, connection, channelManager) -> HttpValueProviderExecutorMessageChannel.builder()
                        .id(Optional.ofNullable(message.getChannelId()).orElseGet(() -> idGenerator.generateId().toString()))
                        .connection(connection)
                        .channelManager(channelManager)
                        .build()
                );
    }

    public void processSubscriberFilters(DefaultMessageExchange exchange) {
        IMessageSubscriberManager subscriberManager = exchange.getSubscriberManager();
        IMessageSubscriberFilterFactory filterFactory = subscriberManager.getFilterFactory();
        filterFactory.register(new MessageTypeMessageSubscriberFilterCreator())
                .register(new P2PMessageSubscriberFilterCreator());
    }

    public void processMessageDecoderFactory(DefaultMessageExchange exchange) {
        exchange.getMessageDecoderFactory().add(new ObjectMapperMessageDecoder(objectMapper));
    }

    public void processSubscriber(DefaultMessageExchange exchange) {
        IMessageSubscriberManager subscriberManager = exchange.getSubscriberManager();
        // 异常订阅者
        subscriberManager.addException((message, e) -> {
            log.error("message exchange error", e);
        });

        // no match subscriber
        subscriberManager.addNoMatch((m) -> true, (message) -> {
            log.info("netty message:{}", message);
            return MessageResult.ack();
        });

        // filter
        exchange.add((message -> {
            log.info("netty message:{}", message);
            return true;
        }));

        // 注册节点接入的消息交换策略
        exchange.add(AntPathMessageMatch.of("catman.cc/core/node/**"), (message -> {
            // 处理节点接入信息,这里只是打印消息
            log.info("node-join [1] message:{}", message);
            return MessageResult.ack();
        }));

        exchange.add(AntPathMessageMatch.of("catman.cc/core/node/**"), (message -> {
            // 处理节点接入信息,这里只是打印消息
            log.info("node-join [2] message:{}", message);
            return MessageResult.ack();
        }));
    }
}
