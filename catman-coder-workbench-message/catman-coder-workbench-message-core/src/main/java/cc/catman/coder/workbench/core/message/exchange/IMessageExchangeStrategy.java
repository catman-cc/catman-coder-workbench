package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.Message;

public interface IMessageExchangeStrategy {
    void exchange(Message<?> message);
}
