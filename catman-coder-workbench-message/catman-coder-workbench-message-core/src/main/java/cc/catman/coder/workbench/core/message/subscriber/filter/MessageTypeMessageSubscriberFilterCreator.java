package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilterCreator;

public class MessageTypeMessageSubscriberFilterCreator implements IMessageSubscriberFilterCreator {
    @Override
    public IMessageSubscriberFilter createFilters(Message<?> message) {
        return new MessageTypeSubscriberFilter(message);
    }
}
