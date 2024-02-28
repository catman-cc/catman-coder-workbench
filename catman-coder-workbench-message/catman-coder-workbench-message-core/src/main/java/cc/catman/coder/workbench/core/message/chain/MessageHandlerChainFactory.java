package cc.catman.coder.workbench.core.message.chain;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

public interface MessageHandlerChainFactory {

    MessageHandlerChain createChain(Message<?> message, MessageResult result);
}
