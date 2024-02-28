package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import cc.catman.coder.workbench.core.message.MessageResult;

import java.util.function.Function;

public class MatchMessageSubscriber implements IMessageSubscriber{
    private MessageMatch messageMatch;
    public Function<Message<?>,MessageResult> onReceive;

    @Override
    public boolean isMatch(Message<?> message) {
        return messageMatch.match(message);
    }

    @Override
    public MessageResult onReceive(Message<?> message) {
       if (onReceive!=null){
           return onReceive.apply(message);
       }
         return null;
    }
}
