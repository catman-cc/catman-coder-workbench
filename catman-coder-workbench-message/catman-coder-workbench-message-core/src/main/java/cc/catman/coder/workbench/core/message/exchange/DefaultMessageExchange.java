package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.MessageType;
import cc.catman.coder.workbench.core.message.exchange.strategy.BroadcastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.exchange.strategy.UnicastMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.subscriber.IMessageFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.subscriber.manager.DefaultMessageSubscriberManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 默认消息交换器
 */
public class DefaultMessageExchange implements IMessageExchange {
    private Map<MessageType,IMessageExchangeStrategy> messageExchangeStrategies;

    @Getter
    private IMessageSubscriberManager subscriberManager;

    public DefaultMessageExchange() {
        this.messageExchangeStrategies=new HashMap<>();
        this.subscriberManager=new DefaultMessageSubscriberManager();
        // 注册默认的消息交换策略
        this.register(MessageType.BROADCAST,new BroadcastMessageExchangeStrategy(this.subscriberManager));
        this.register(MessageType.UNICAST,new UnicastMessageExchangeStrategy(this.subscriberManager));
    }

    public DefaultMessageExchange(Map<MessageType, IMessageExchangeStrategy> messageExchangeStrategies) {
        this.messageExchangeStrategies = messageExchangeStrategies;
    }

    @Override
    public void exchange(Message<?> message) {

        this.subscriberManager.filters().forEach(filter->filter.filter(message));

        IMessageExchangeStrategy messageExchangeStrategy = messageExchangeStrategies.get(message.getType());
        if (messageExchangeStrategy != null){
            messageExchangeStrategy.exchange(message);
        }
    }

    public DefaultMessageExchange register(MessageType messageType,IMessageExchangeStrategy messageExchangeStrategy){
        messageExchangeStrategies.put(messageType,messageExchangeStrategy);
        return this;
    }

    public DefaultMessageExchange unregister(MessageType messageType){
        messageExchangeStrategies.remove(messageType);
        return this;
    }

    public DefaultMessageExchange add(IMessageSubscriber subscriber){
        subscriberManager.addSubscriber(subscriber);
        return this;
    }

    public DefaultMessageExchange add(MessageMatch match, Function<Message<?>, MessageResult> func){
        subscriberManager.add(match,func);
        return this;
    }

    public DefaultMessageExchange add(IMessageFilter filter){
        subscriberManager.add(filter);
        return this;
    }

    public DefaultMessageExchange remove(IMessageFilter  filter){
        subscriberManager.remove(filter);
        return this;
    }
}
