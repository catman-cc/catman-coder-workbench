package cc.catman.coder.workbench.core.message;

/**
 * 消息总线,用于消息的发布和订阅
 */
public interface IMessageBus {
    /**
     * 发布消息
     *
     * @param message 消息
     */
    void publish(Message message);

    /**
     * 订阅消息
     *
     * @param subscriber 订阅者
     */
    void subscribe(MessageSubscriber subscriber);

}
