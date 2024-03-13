package cc.catman.coder.workbench.core.message.exchange.strategy;

import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.exception.MessageExchangeStrategyNotFoundRuntimeException;
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
    public MessageResult exchange(Message<?> message) {
        // 执行路由策略
        // 🤔,简陋的处理机制,不过目前足以应对业务
        MessageResult result=MessageResult.drop();
        // TODO 或许此处应该构建一个消息处理链,而不是简单的遍历
        // 消息处理链处理后返回最终的MessageResult,然后根据MessageResult进行后续处理
        try {
            result= doExchange(message);
            if (message.getCount()==0){
                // 没有匹配的消息处理器
                subscriberManager.noMatchMessageSubscriber()
                        .forEach(subscriber -> subscriber.onReceive(message));
            }
        } catch (Exception e) {
            // 异常处理
            subscriberManager.exceptionMessageSubscriber()
                    .forEach(subscriber -> subscriber.onError(message,new MessageExchangeStrategyNotFoundRuntimeException(e,message)));
        }
        return result;
    }

    public abstract MessageResult doExchange(Message<?> message);
}
