package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.subscriber.IMessageFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;

import java.util.function.Function;

/**
 * 消息交换器,用于消息的交换
 */
public interface IMessageExchange {
    /**
     * 接收消息,返回是否接收成功
     *
     * @param message 消息
     */
    void exchange(Message<?> message);

    IMessageExchange register(MessageType messageType, IMessageExchangeStrategy messageExchangeStrategy);

    IMessageExchange unregister(MessageType messageType);

    IMessageExchange add(IMessageSubscriber subscriber);

    IMessageSubscriber add(MessageMatch match, Function<Message<?>, MessageResult> func);

    IMessageExchange add(IMessageFilter filter);

    IMessageExchange remove(IMessageFilter filter);

    IMessageSubscriberManager getSubscriberManager();

    IMessageDecoderFactory getMessageDecoderFactory();
}
