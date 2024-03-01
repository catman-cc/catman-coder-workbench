package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelManager;
import cc.catman.coder.workbench.core.message.exception.MessageExchangeStrategyNotFoundRuntimeException;
import cc.catman.coder.workbench.core.message.exchange.strategy.BroadcastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.exchange.strategy.UnicastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.subscriber.IMessageFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.subscriber.manager.DefaultMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.validator.IMessageValidator;
import cc.catman.coder.workbench.core.message.validator.ValidateResult;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;

/**
 * 默认消息交换器
 */
public class DefaultMessageExchange implements IMessageExchange {
    private Map<MessageType, IMessageExchangeStrategy> messageExchangeStrategies;

    private List<IMessageValidator> messageValidators;
    @Getter
    private IMessageSubscriberManager subscriberManager;

    @Getter
    private IMessageDecoderFactory messageDecoderFactory;
    @Getter
    private ChannelManager channelManager;

    public DefaultMessageExchange() {
        this.messageExchangeStrategies = new HashMap<>();
        this.subscriberManager = new DefaultMessageSubscriberManager();
        this.messageDecoderFactory= new DefaultMessageDecoderFactory();
        this.channelManager=new DefaultChannelManager();
        this.messageValidators=new ArrayList<>();
        // 注册默认的消息交换策略
        this.register(MessageType.BROADCAST, new BroadcastMessageExchangeStrategy(this.subscriberManager));
        this.register(MessageType.UNICAST, new UnicastMessageExchangeStrategy(this.subscriberManager));
    }

    public DefaultMessageExchange(Map<MessageType, IMessageExchangeStrategy> messageExchangeStrategies) {
        this.messageExchangeStrategies = messageExchangeStrategies;
    }

    @Override
    public void exchange(Message<?> message, MessageConnection<?> connection) {
        if (message == null) {
            return;
        }
        if (Optional.ofNullable(message.getType()).isEmpty()) {
            message.setType(MessageType.UNICAST);
        }
        // 填充类型转换器
        if (Optional.ofNullable(message.getMessageDecoderFactory()).isEmpty()) {
            message.setMessageDecoderFactory(this.messageDecoderFactory);
        }
        //  填充消息交换器
        if (Optional.ofNullable(message.getMessageExchange()).isEmpty()){
            message.setMessageExchange(this);
        }
        // 填充消息信道,除了system之外,其余的消息在交换时,都需要信道已经存在
       channelManager.getOrCreateChannel(message, connection);

        this.subscriberManager.filters().forEach(filter -> filter.filter(message));

        IMessageExchangeStrategy messageExchangeStrategy = messageExchangeStrategies.get(message.getType());

        if (messageExchangeStrategy == null) {
            // 没有找到对应的策略,属于异常消息,所以触发异常消息订阅者
            this.subscriberManager.onError(message, new MessageExchangeStrategyNotFoundRuntimeException(message));
            return;
        }

        // 消息验证
        for (IMessageValidator messageValidator : messageValidators) {
            ValidateResult res=messageValidator.validate(message);
            if (!res.isSuccess()) {
                // 验证失败,触发异常消息订阅者
                this.subscriberManager.onError(message, new RuntimeException(res.getMessage()));
            }
        }

        messageExchangeStrategy.exchange(message);
    }

    @Override
    public DefaultMessageExchange register(MessageType messageType, IMessageExchangeStrategy messageExchangeStrategy) {
        messageExchangeStrategies.put(messageType, messageExchangeStrategy);
        return this;
    }

    @Override
    public DefaultMessageExchange unregister(MessageType messageType) {
        messageExchangeStrategies.remove(messageType);
        return this;
    }

    @Override
    public DefaultMessageExchange add(IMessageSubscriber subscriber) {
        subscriberManager.addSubscriber(subscriber);
        return this;
    }

    @Override
    public IMessageSubscriber add(MessageMatch match, Function<Message<?>, MessageResult> func) {
        return subscriberManager.add(match, func);
    }

    @Override
    public DefaultMessageExchange add(IMessageFilter filter) {
        subscriberManager.add(filter);
        return this;
    }

    @Override
    public DefaultMessageExchange remove(IMessageFilter filter) {
        subscriberManager.remove(filter);
        return this;
    }
}
