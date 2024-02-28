package cc.catman.coder.workbench.core.message.handler;

import cc.catman.coder.workbench.core.message.IMessageHandler;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageExchange;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.chain.MessageHandlerChain;

/**
 *  具有发布订阅功能的消息处理器
 */
public class DefaultMessageHandler implements IMessageHandler<Object> {

    private MessageExchange messageExchange;
    @Override
    public void handle(Message<?> message, MessageResult result, MessageHandlerChain chain) {
    }
}
