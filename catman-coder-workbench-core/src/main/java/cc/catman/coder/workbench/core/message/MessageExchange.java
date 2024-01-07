package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;

/**
 * 消息交换器,用于消息的交换
 */
public interface MessageExchange {
    /**
     * 接收消息,返回是否接收成功
     *
     * @param message 消息
     */
    MessageACK route(JsonTreeMessage message);

    void subscribe(MessageSubscriber subscriber);
}
