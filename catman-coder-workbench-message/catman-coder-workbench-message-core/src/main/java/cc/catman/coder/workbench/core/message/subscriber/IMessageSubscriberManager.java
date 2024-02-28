package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import cc.catman.coder.workbench.core.message.MessageResult;

import java.util.List;
import java.util.function.Function;

/**
 * 消息订阅者管理器,用于管理消息订阅者
 */
public interface IMessageSubscriberManager {
    /**
     * 添加订阅者
     *
     * @param subscriber 订阅者
     */
    void addSubscriber(IMessageSubscriber subscriber);

    /**
     * 移除订阅者
     *
     * @param subscriber 订阅者
     */
    void removeSubscriber(IMessageSubscriber subscriber);

    default void unsubscribe(IMessageSubscriber subscriber){
        removeSubscriber(subscriber);
    }

    /**
     * 获取订阅者
     */
    List<IMessageSubscriber> list();

    List<IMessageFilter> filters();

    List<IMessageSubscriber> noMatchMessageSubscriber();

    List<IMessageSubscriber> exceptionMessageSubscriber();

    void triggerWatchBefore(Message<?> message, IMessageSubscriber subscriber);
    void triggerWatchAfter(Message<?> message, IMessageSubscriber subscriber);

    void addWatcher(IMessageSubscribeWatcher watcher);

    void removeWatcher(IMessageSubscribeWatcher watcher);

    void add(IMessageFilter filter);

    void add(MessageMatch match, Function<Message<?>, MessageResult> func);

    void remove(IMessageFilter filter);
}
