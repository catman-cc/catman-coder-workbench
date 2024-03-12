package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilter;

import java.util.Optional;

public abstract class AbstractMessageSubscriberFilter implements IMessageSubscriberFilter {
    protected Message<?> message;

    public AbstractMessageSubscriberFilter(Message<?> message) {
        this.message = message;
    }

    @Override
    public Message<?> getMessage() {
        return message;
    }

    protected boolean hasAttrs(IMessageSubscriber subscriber,String ...attrs){
        for (String attr : attrs) {
            if (Optional.ofNullable(subscriber.getAttr(attr)).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
