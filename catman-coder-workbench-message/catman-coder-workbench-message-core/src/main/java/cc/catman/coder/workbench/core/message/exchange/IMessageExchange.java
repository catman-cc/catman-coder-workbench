package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.Message;

/**
 * 消息交换器,用于消息的交换
 */
public interface IMessageExchange {
    /**
     * 接收消息,返回是否接收成功
     *
     * @param message 消息
     */
    void exchange(Message<?> message);
}
