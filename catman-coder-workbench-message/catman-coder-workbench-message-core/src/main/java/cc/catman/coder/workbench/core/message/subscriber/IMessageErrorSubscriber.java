package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

public interface IMessageErrorSubscriber {

    /**
     * 是否匹配消息
     *
     * @param message 消息
     * @return 是否匹配
     */
    boolean isMatch(Message<?> message);

    /**
     * 接收消息,返回是否接收成功
     *
     * @param message 消息
     */
    void onError(Message<?> message,Throwable error);
}
