package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelManager;
import cc.catman.coder.workbench.core.message.exception.MessageExchangeStrategyNotFoundRuntimeException;
import cc.catman.coder.workbench.core.message.exchange.strategy.BroadcastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.exchange.strategy.UnicastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.subscriber.*;
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
    private final Map<MessageType, IMessageExchangeStrategy> messageExchangeStrategies;

    /**
     * 消息验证器,用于验证消息的合法性
     */
    private List<IMessageValidator> messageValidators;

    /**
     * 消息订阅者管理器,管理并维护消息订阅者
     */
    @Getter
    private IMessageSubscriberManager subscriberManager;

    /**
     * 消息解码器工厂,用于创建消息解码器,完成消息载荷的解码
     */
    @Getter
    private IMessageDecoderFactory messageDecoderFactory;
    /**
     * 消息信道管理器,用于管理消息的信道
     */
    @Getter
    private ChannelManager channelManager;

    public DefaultMessageExchange() {
        this.messageExchangeStrategies = new HashMap<>();
        this.subscriberManager = new DefaultMessageSubscriberManager();
        this.messageDecoderFactory= new DefaultMessageDecoderFactory();
        this.channelManager=DefaultChannelManager.builder().build();
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

        Message<?> fm = message;
        this.subscriberManager.filters().forEach(filter -> filter.filter(fm));

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
        for (IMessageSurround messageSurround : this.subscriberManager.surrounds()) {
            message=messageSurround.before(message);
        }
        MessageResult res =MessageResult.drop();
        try {
            res = messageExchangeStrategy.exchange(message);
        }catch (Exception e) {
            for (IMessageSurround messageSurround : this.subscriberManager.surrounds()) {
                messageSurround.onError(message,e,res);
            }
        }

        for (IMessageSurround messageSurround : this.subscriberManager.surrounds()) {
            messageSurround.after(message,res);
        }
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
        if (subscriber instanceof PostExchangeInjectMessageSubscriber){
            ((PostExchangeInjectMessageSubscriber) subscriber).injectMessageExchange(this);
        }
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
