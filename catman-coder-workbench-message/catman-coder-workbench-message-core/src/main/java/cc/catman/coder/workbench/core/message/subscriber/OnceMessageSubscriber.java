package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.MessageType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OnceMessageSubscriber implements IMessageSubscriber{
    private IMessageSubscriber subscriber;

    @Override
    public List<MessageType> supportMessageTypes() {
        return subscriber.supportMessageTypes();
    }

    @Override
    public Map<String, Object> attributes() {
       return subscriber.attributes();
    }

    @Override
    public <T> T getAttr(String key) {
        return subscriber.getAttr(key);
    }

    @Override
    public void close() throws IOException {
         subscriber.close();
    }

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
