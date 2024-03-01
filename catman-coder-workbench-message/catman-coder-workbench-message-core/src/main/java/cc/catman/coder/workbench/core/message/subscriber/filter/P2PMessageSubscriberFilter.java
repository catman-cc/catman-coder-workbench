package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 点对点消息订阅者过滤器,用于过滤消息订阅者
 * 要求订阅者包含点对点消息的相关属性,主要是匹配的接收者ID和发送者ID
 * 该过滤器和其他过滤器不同的是,如果过滤器不包含接收者ID和发送者ID,则不会进行过滤
 */
public class P2PMessageSubscriberFilter extends AbstractMessageSubscriberFilter {
    public static final String PROPERTY_RECEIVER_NAME = "receiverId";
    public static final String PROPERTY_SENDER_NAME = "senderId";

    protected final boolean needFilter;

    public P2PMessageSubscriberFilter(Message<?> message) {
        super(message);
        this.needFilter = needFilter(message);
    }

    @Override
    public List<IMessageSubscriber> filter(List<IMessageSubscriber> subscribers) {
        if (this.needFilter) {
            return subscribers.stream()
                    .filter(subscriber -> hasRequiredProperties(subscriber) && subscriber.isMatch(message))
                    .filter(subscriber -> {
                        String receiverId = (String) subscriber.attributes().get(PROPERTY_RECEIVER_NAME);
                        String senderId = (String) subscriber.attributes().get(PROPERTY_SENDER_NAME);
                        return receiverId.equals(message.getReceiver()) && senderId.equals(message.getSender());
                    })
                    .toList();
        }
        return subscribers;
    }

    protected static boolean needFilter(Message<?> message){
        return StringUtils.hasText(message.getReceiver()) && StringUtils.hasText(message.getSender());
    }

    protected boolean hasRequiredProperties(IMessageSubscriber subscriber) {
        return hasProperties(subscriber,PROPERTY_RECEIVER_NAME) && hasProperties(subscriber,PROPERTY_SENDER_NAME);
    }

    protected boolean hasProperties(IMessageSubscriber subscriber,String propertyName) {
        return subscriber.attributes().containsKey(propertyName);
    }
}
