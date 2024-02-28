package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

@FunctionalInterface
public interface IMessageFilter {
    void filter(Message<?> message);
}
