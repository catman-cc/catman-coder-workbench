package cc.catman.coder.workbench.core.message;


import cc.catman.coder.workbench.core.message.chain.MessageHandlerChain;

public interface IMessageHandler<T> {
    void handle(Message<?> message, MessageResult result, MessageHandlerChain chain);
}
