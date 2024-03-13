package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

public interface IMessageSurround {

    default Message<?> before(Message<?> message){
        return message;
    }

    default void after(Message<?> message, MessageResult result){
    }

    default void onError(Message<?>message,Throwable e,MessageResult result){
    }

}
