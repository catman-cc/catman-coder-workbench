package cc.catman.coder.workbench.core.message.chain;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

public class DefaultMessageHandlerChainFactory implements MessageHandlerChainFactory{

    @Override
    public MessageHandlerChain createChain(Message<?> message, MessageResult result) {
        return new DefaultMessageHandlerChain();
    }
}
