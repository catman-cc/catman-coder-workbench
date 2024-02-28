package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.channel.DefaultMessageChannel;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelFactory;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelManager;
import cc.catman.coder.workbench.core.message.channel.HttpValueProviderExecutorMessageChannel;
import cc.catman.coder.workbench.core.message.exchange.Default1MessageExchange;
import cc.catman.coder.workbench.core.value.ValueProviderRegistry;
import cc.catman.workbench.api.server.websocket.run.value.ExecutorMessageSubscriber;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

import jakarta.annotation.Resource;

@Configuration
public class MessageConfiguration {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ModelMapper modelMapper;

    @Resource
    private ValueProviderExecutor executor;

    @Resource
    private ValueProviderRegistry valueProviderRegistry;
    @Resource
    private GenericConversionService convertService;

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
    public MessageExchange messageExchange() {
        Default1MessageExchange exchange = Default1MessageExchange.builder()
                .modelMapper(modelMapper)
                .objectMapper(objectMapper)
                .build();

        ExecutorMessageSubscriber
                .builder()
                .executor(executor)
                .modelMapper(modelMapper)
                .objectMapper(objectMapper)
                .convertService(convertService)
                .valueProviderRegistry(valueProviderRegistry)
                .messageQueue(new GroupedMessageQueue<>((key, msg, groupedMessageQueue) -> msg::answer))
                .build().subscribe(exchange);

        return exchange;
    }

}
