package cc.catman.coder.workbench.core.message;

/**
 * 消息客户端
 */
public interface IMessageClient {
    /**
     * 创建一个消息信道
     * @return 消息信道
     */
    MessageChannel createChannel();
}
