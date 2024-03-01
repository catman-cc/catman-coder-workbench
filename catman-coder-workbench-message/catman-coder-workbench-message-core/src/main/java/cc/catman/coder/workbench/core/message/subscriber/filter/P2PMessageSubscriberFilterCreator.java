package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilter;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberFilterCreator;

public class P2PMessageSubscriberFilterCreator implements IMessageSubscriberFilterCreator {
    @Override
    public boolean supports(Message<?> message) {
        return P2PMessageSubscriberFilter.needFilter(message);
    }

    @Override
    public IMessageSubscriberFilter createFilters(Message<?> message) {
        return new P2PMessageSubscriberFilter(message);
    }
}
