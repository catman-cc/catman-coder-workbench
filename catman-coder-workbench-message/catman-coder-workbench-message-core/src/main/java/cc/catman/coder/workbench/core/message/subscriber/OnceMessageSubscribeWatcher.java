package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnceMessageSubscribeWatcher implements IMessageSubscribeWatcher{
    @Override
    public void onWatchAfter(Message<?> message, IMessageSubscriber subscriber, IMessageSubscriberManager manager) {
        if (subscriber instanceof OnceMessageSubscriber){
            // 取消订阅
            manager.unsubscribe(subscriber);
            log.debug("Unsubscribe once message subscriber: {}", subscriber);
        }
    }
}
