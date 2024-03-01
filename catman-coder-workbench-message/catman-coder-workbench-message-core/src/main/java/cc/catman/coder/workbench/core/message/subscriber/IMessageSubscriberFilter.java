package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

import java.util.List;

/**
 * 消息订阅者过滤器,用于过滤消息订阅者
 * 该过滤器会在消息订阅者接收消息之前进行过滤
 */
public interface IMessageSubscriberFilter {
    Message<?> getMessage();

    List<IMessageSubscriber> filter(List<IMessageSubscriber> subscribers);
}
