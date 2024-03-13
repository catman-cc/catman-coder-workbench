package cc.catman.coder.workbench.core.message;

/**
 * 消息处理器回调
 */
public interface MessageHandlerCallback {
    void callback(Message<?> message);
}
