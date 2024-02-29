package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchMessageErrorSubscriber implements IMessageErrorSubscriber{
    private MessageMatch messageMatch;
    private ErrorHandler onError;
    @Override
    public boolean isMatch(Message<?> message) {
        return messageMatch.match(message);
    }

    @Override
    public void onError(Message<?> message, Throwable error) {
         this.onError.onError(message,error);
    }

    @FunctionalInterface
    public static interface ErrorHandler{
        void onError(Message<?> message, Throwable error);
    }
}
