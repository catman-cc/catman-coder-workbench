package cc.catman.coder.workbench.core.message.exchange.strategy;

import cc.catman.coder.workbench.core.message.exchange.IMessageExchangeStrategy;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.Message;

public abstract class AbstractMessageExchangeStrategy implements IMessageExchangeStrategy {

    /**
     * 消息订阅者管理器
     */
    protected IMessageSubscriberManager subscriberManager;

    public AbstractMessageExchangeStrategy(IMessageSubscriberManager subscriberManager) {
        this.subscriberManager = subscriberManager;
    }

    @Override
    public void exchange(Message<?> message) {
        // 执行路由策略
        // 🤔,简陋的处理机制,不过目前足以应对业务
        try {
            doExchange(message);
            if (message.getCount()==0){
                // 没有匹配的消息处理器
                subscriberManager.noMatchMessageSubscriber()
                        .forEach(subscriber -> subscriber.onReceive(message));
            }
        } catch (Exception e) {
            // 异常处理
            subscriberManager.exceptionMessageSubscriber()
                    .forEach(subscriber -> subscriber.onReceive(message));
        }
    }

    public abstract void doExchange(Message<?> message);
}
