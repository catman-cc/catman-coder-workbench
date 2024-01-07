package cc.catman.coder.workbench.core.message;

/**
 * 消息上下文,上下文应该持有对应的信道
 */
public interface MessageContext<T> {

    T getVariable();

    MessageChannel getChannel();

    /**
     * 发送消息
     * @param message 消息
     * @param callback 回调
     * @return 消息应答
     */
    MessageACK send(Message<?> message,MessageHandlerCallback<?> callback);
}
