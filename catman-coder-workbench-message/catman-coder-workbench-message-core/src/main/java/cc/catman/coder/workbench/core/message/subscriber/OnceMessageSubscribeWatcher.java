package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;

public class OnceMessageSubscribeWatcher implements IMessageSubscribeWatcher{
    @Override
    public void onWatchAfter(Message<?> message, IMessageSubscriber subscriber, IMessageSubscriberManager manager) {
        if (subscriber instanceof OnceMessageSubscriber){
            // 取消订阅
            manager.unsubscribe(subscriber);
        }
    }
}
