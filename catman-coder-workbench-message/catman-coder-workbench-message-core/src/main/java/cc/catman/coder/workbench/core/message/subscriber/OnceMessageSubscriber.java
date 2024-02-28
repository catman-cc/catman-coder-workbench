package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

public class OnceMessageSubscriber implements IMessageSubscriber{
    private IMessageSubscriber subscriber;

    public static OnceMessageSubscriber of(IMessageSubscriber subscriber){
        OnceMessageSubscriber onceMessageSubscriber = new OnceMessageSubscriber();
        onceMessageSubscriber.subscriber = subscriber;
        return onceMessageSubscriber;
    }

    @Override
    public boolean isMatch(Message<?> message) {
        return subscriber.isMatch(message);
    }

    @Override
    public MessageResult onReceive(Message<?> message) {
        return subscriber.onReceive(message);
    }
}
