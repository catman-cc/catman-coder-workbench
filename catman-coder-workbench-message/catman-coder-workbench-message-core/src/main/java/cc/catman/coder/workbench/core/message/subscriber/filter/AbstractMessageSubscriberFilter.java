package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilter;

public abstract class AbstractMessageSubscriberFilter implements IMessageSubscriberFilter {
    protected Message<?> message;

    public AbstractMessageSubscriberFilter(Message<?> message) {
        this.message = message;
    }

    @Override
    public Message<?> getMessage() {
        return message;
    }
}
