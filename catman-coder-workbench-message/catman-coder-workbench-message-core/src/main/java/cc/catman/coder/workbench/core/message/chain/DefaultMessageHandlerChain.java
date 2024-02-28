package cc.catman.coder.workbench.core.message.chain;

import cc.catman.coder.workbench.core.message.IMessageHandler;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DefaultMessageHandlerChain implements MessageHandlerChain {
    @Getter
    @Setter
    private List<IMessageHandler<?>> handlers;
    private int index = 0;

    public DefaultMessageHandlerChain() {
        this(new ArrayList<>());
    }

    public DefaultMessageHandlerChain(List<IMessageHandler<?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void next(Message<?> message, MessageResult result) {
        if (index < handlers.size()) {
            IMessageHandler<?> handler = handlers.get(index++);
            handler.handle(message, result, this);
        }
    }



    public DefaultMessageHandlerChain addHandler(IMessageHandler<?> handler) {
        handlers.add(handler);
        return this;
    }

    public DefaultMessageHandlerChain removeHandler(IMessageHandler<?> handler) {
        handlers.remove(handler);
        return this;
    }

    public DefaultMessageHandlerChain removeHandler(int index) {
        handlers.remove(index);
        return this;
    }

    public DefaultMessageHandlerChain clear() {
        handlers.clear();
        return this;
    }

    public int size() {
        return handlers.size();
    }

    public IMessageHandler<?> getHandler(int index) {
        return handlers.get(index);
    }

    public void setHandler(int index, IMessageHandler<?> handler) {
        handlers.set(index, handler);
    }

    public void addHandler(int index, IMessageHandler<?> handler) {
        handlers.add(index, handler);
    }

    public void addHandlers(List<IMessageHandler<?>> handlers) {
        this.handlers.addAll(handlers);
    }

    public void addHandlers(int index, List<IMessageHandler<?>> handlers) {
        this.handlers.addAll(index, handlers);
    }

    public void removeHandlers(List<IMessageHandler<?>> handlers) {
        this.handlers.removeAll(handlers);
    }

    public void removeHandlers(int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            handlers.remove(i);
        }
    }


}
