package cc.catman.coder.workbench.core.message.exchange.strategy;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;

import java.util.List;
import java.util.Optional;

/**
 * 单播消息交换策略,该策略只会将消息发送给第一个匹配的消息处理器
 */
public class UnicastMessageExchangeStrategy extends AbstractMessageExchangeStrategy{

    public UnicastMessageExchangeStrategy(IMessageSubscriberManager subscriberManager) {
        super(subscriberManager);
    }

    @Override
    public void doExchange(Message<?> message) {
        List<IMessageSubscriber> subscribers = this.subscriberManager.list(message);
        Optional<IMessageSubscriber> find = subscribers.stream()
                .filter(subscriber -> subscriber.isMatch(message))
                .findFirst();
        if (find.isPresent()) {
            IMessageSubscriber subscriber = find.get();
            this.subscriberManager.triggerWatchBefore(message,subscriber );
            MessageResult ignored = subscriber.onReceive(message);
            message.increment();
            this.subscriberManager.triggerWatchAfter(message,subscriber );
        }
    }
}
