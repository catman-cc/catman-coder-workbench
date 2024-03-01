package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;

import java.util.List;
import java.util.function.Predicate;

/**
 * 消息信道订阅者过滤器,用于过滤消息订阅者
 * 只讲订阅者传递给特定的消息信道
 */
public class ChannelMessageSubscriberFilter extends AbstractMessageSubscriberFilter{

    public ChannelMessageSubscriberFilter(Message<?> message) {
        super(message);
    }

    /**
     * 过滤消息订阅者
     * @param subscribers 消息订阅者
     * @return 过滤后的消息订阅者
     */
    @Override
    public List<IMessageSubscriber> filter(List<IMessageSubscriber> subscribers) {
        // 如果消息设置了信道id,表示消息只发送给特定的信道,需要过滤掉不支持该信道的订阅者
        String channelId = message.getChannelId();
        if (channelId != null) {
            return subscribers.stream()
                    .filter(subscriber -> {
                         Object predicate = subscriber.getAttr("channel-test");
                         if (predicate instanceof Predicate<?> p) {
                             Predicate<String> pre= (Predicate<String>) p;
                             return pre.test(channelId);
                         }
                            return false;
                    })
                    .toList();
        }
        return subscribers;
    }
}
