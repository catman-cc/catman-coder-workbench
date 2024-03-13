package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

public interface IMessageExchangeStrategy {
    MessageResult exchange(Message<?> message);
}
