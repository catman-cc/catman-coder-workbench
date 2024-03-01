package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.exception.MessageExchangeStrategyNotFoundRuntimeException;
import cc.catman.coder.workbench.core.message.exchange.strategy.BroadcastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.exchange.strategy.UnicastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.subscriber.IMessageFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.subscriber.manager.DefaultMessageSubscriberManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 默认消息交换器
 */
public class DefaultMessageExchange implements IMessageExchange {
    private Map<MessageType, IMessageExchangeStrategy> messageExchangeStrategies;

    @Getter
    private IMessageSubscriberManager subscriberManager;

    @Getter
    private IMessageDecoderFactory messageDecoderFactory;

    public DefaultMessageExchange() {
        this.messageExchangeStrategies = new HashMap<>();
        this.subscriberManager = new DefaultMessageSubscriberManager();
        this.messageDecoderFactory= new DefaultMessageDecoderFactory();
        // 注册默认的消息交换策略
        this.register(MessageType.BROADCAST, new BroadcastMessageExchangeStrategy(this.subscriberManager));
        this.register(MessageType.UNICAST, new UnicastMessageExchangeStrategy(this.subscriberManager));
    }

    public DefaultMessageExchange(Map<MessageType, IMessageExchangeStrategy> messageExchangeStrategies) {
        this.messageExchangeStrategies = messageExchangeStrategies;
    }

    @Override
    public void exchange(Message<?> message) {
        if (message == null) {
            return;
        }
        // 填充类型转换器
        if (Optional.ofNullable(message.getMessageDecoderFactory()).isEmpty()) {
            message.setMessageDecoderFactory(this.messageDecoderFactory);
        }
        //  填充消息交换器
        if (Optional.ofNullable(message.getMessageExchange()).isEmpty()){
            message.setMessageExchange(this);
        }

        this.subscriberManager.filters().forEach(filter -> filter.filter(message));

        IMessageExchangeStrategy messageExchangeStrategy = messageExchangeStrategies.get(message.getType());

        if (messageExchangeStrategy == null) {
            // 没有找到对应的策略,属于异常消息,所以触发异常消息订阅者
            this.subscriberManager.onError(message, new MessageExchangeStrategyNotFoundRuntimeException(message));
            return;
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
