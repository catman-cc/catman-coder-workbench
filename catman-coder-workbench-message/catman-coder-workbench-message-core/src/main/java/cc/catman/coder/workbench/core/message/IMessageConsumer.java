package cc.catman.coder.workbench.core.message;

/**
 * 消息消费者
 */
public interface IMessageConsumer {

    /**
     * 过滤消息
     * @param message 消息
     * @return 过滤结果, true表示通过, false表示拒绝
     */
    boolean filter(Message<?> message);

    void consume(Message<?> message, MessageResult result);

}
