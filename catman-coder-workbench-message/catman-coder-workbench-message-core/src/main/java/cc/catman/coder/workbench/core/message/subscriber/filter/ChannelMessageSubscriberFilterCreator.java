package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilterCreator;

/**
 * 消息信道订阅者过滤器创建者
 */
public class ChannelMessageSubscriberFilterCreator implements IMessageSubscriberFilterCreator {
    @Override
    public boolean supports(Message<?> message) {
        // 消息设置了信道ID
        String channelId = message.getChannelId();
        return channelId != null;
    }

    @Override
    public IMessageSubscriberFilter createFilters(Message<?> message) {
        return new ChannelMessageSubscriberFilter(message);
    }
}
