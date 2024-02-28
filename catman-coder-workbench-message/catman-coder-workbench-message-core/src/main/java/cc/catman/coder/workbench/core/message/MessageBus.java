package cc.catman.coder.workbench.core.message;

/**
 * 消息总线,用于消息的发布和订阅
 */
public interface MessageBus {
    void publish(Message message);

    void subscribe(MessageSubscriber subscriber);

}
