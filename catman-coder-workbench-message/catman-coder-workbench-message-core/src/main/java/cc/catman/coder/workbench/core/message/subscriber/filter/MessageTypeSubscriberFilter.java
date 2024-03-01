package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;

import java.util.List;

/**
 * 消息类型订阅者过滤器,用于过滤消息订阅者
 */
public class MessageTypeSubscriberFilter extends AbstractMessageSubscriberFilter {

    public MessageTypeSubscriberFilter(Message<?> message) {
        super(message);
    }

    @Override
    public List<IMessageSubscriber> filter(List<IMessageSubscriber> subscribers) {
        return subscribers.stream()
                .filter(subscriber -> subscriber.supportMessageTypes()
                        .stream()
                        .anyMatch(messageType -> messageType.equals(message.getType())))
                .toList();
    }
}
