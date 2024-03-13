package cc.catman.coder.workbench.core.message.context;

import cc.catman.coder.workbench.core.message.*;

/**
 *
 */
public class DefaultMessageContext<T> implements MessageContext<T> {
    private T variable;
    private MessageChannel messageChannel;

    @Override
    public T getVariable() {
        return variable;
    }

    @Override
    public MessageChannel getChannel() {
        return this.messageChannel;
    }

    @Override
    public MessageACK send(Message<?> message, MessageHandlerCallback callback) {
       return this.messageChannel.send(message);
    }
}
