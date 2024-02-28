package cc.catman.coder.workbench.core.message;
/**
 * 消息上下文管理器
 */
public interface MessageContextManager {
    MessageContext getOrCreateContext(Message<?> message,MessageConnection<?> connection);
}
