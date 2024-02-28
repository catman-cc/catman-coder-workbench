package cc.catman.coder.workbench.core.message.chain;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

/**
 * 消息处理器链,用于处理消息
 */
public interface MessageHandlerChain {

    void next(Message<?> message, MessageResult result);
}
