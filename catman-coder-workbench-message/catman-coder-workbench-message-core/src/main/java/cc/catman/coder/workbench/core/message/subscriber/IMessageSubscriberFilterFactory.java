package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

import java.util.List;

public interface IMessageSubscriberFilterFactory {
    List<IMessageSubscriberFilter> createFilters(Message<?> message);

    IMessageSubscriberFilterFactory register(IMessageSubscriberFilterCreator filter);
}
