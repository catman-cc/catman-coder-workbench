package cc.catman.coder.workbench.core.message;

public interface MessageMatch {
    /**
     * 匹配消息
     *
     * @param message 消息
     * @return 是否匹配
     */
    boolean match(Message<?> message);
}
