package cc.catman.coder.workbench.core.message.exchange.strategy;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;

import java.util.List;

/**
 * 广播消息交换策略,该策略会将消息发送给所有的订阅者
 */
public class BroadcastMessageExchangeStrategy extends AbstractMessageExchangeStrategy{
    public BroadcastMessageExchangeStrategy(IMessageSubscriberManager subscriberManager) {
        super(subscriberManager);
    }

    @Override
    public MessageResult doExchange(Message<?> message) {
        List<IMessageSubscriber> subscribers = this.subscriberManager.list(message);

        MessageResult result=MessageResult.ack();
        for (IMessageSubscriber subscriber : subscribers.stream()
                .filter(subscriber->subscriber.isMatch(message)).toList()
        ) {
            this.subscriberManager.triggerWatchBefore(message, subscriber);
            result = subscriber.onReceive(message);
            message.increment();
            this.subscriberManager.triggerWatchAfter(message, subscriber);
        }
        return result;
    }

}
