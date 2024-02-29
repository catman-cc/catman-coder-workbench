package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

@FunctionalInterface
public interface IMessageFilter {
    /**
     * 统一消息过滤器
     * @param message 消息
     */
    boolean filter(Message<?> message);
}
