package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

/**
 * 消息订阅监听器,其可以访问到消息订阅器,并有针对性的调度消息订阅器
 * 比如,一次性订阅,在匹配到一条消息后,就取消订阅
 */
public interface IMessageSubscribeWatcher {
   default void start(){}

    default void onWatchBefore(Message<?> message, IMessageSubscriber subscriber, IMessageSubscriberManager manager){}

   default void onWatchAfter(Message<?> message, IMessageSubscriber subscriber, IMessageSubscriberManager manager){}

   default void stop(){}
}
