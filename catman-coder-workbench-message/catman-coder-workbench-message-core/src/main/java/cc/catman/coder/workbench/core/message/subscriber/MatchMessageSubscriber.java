package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import cc.catman.coder.workbench.core.message.MessageResult;
import lombok.Builder;

import java.util.Map;
import java.util.function.Function;

@Builder
public class MatchMessageSubscriber implements IMessageSubscriber{

    private MessageMatch messageMatch;
    private Map<String,Object> attributes;
    public Function<Message<?>,MessageResult> onReceive;

    @Override
    public Map<String, Object> attributes() {
        return attributes;
    }

    public <T> T getAttr(String key){
        return (T) attributes().get(key);
    }

    @Override
    public boolean isMatch(Message<?> message) {
        if (messageMatch==null){
            return true;
        }
        return messageMatch.match(message);
    }

    @Override
    public MessageResult onReceive(Message<?> message) {
       if (onReceive!=null){
           return onReceive.apply(message);
       }
         return MessageResult.drop();
    }
}
