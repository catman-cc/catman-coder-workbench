package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultMessageSubscriberFilterFactory implements IMessageSubscriberFilterFactory{

    private final List<IMessageSubscriberFilterCreator> filters=new ArrayList<>();

    @Override
    public List<IMessageSubscriberFilter> createFilters(Message<?> message) {
        return this.filters.stream()
                .filter(filter->filter.supports(message))
                .peek(filter->log.debug("create filter for message:{},filter type:{}",message.getId(),filter.getClass().getName()))
                .map(filter->filter.createFilters(message))
                .toList();
    }

    @Override
    public IMessageSubscriberFilterFactory register(IMessageSubscriberFilterCreator creator) {
         log.debug("register filter creator:{}",creator.getClass().getName());
         this.filters.add(creator);
         return this;
    }
}
