package cc.catman.coder.workbench.core.message.exception;

import cc.catman.coder.workbench.core.message.Message;

public class MessageExchangeStrategyNotFoundRuntimeException extends RuntimeException {
    private Message<?> message;

    public MessageExchangeStrategyNotFoundRuntimeException(Message<?> message) {
        this.message = message;
    }

    public MessageExchangeStrategyNotFoundRuntimeException(String message, Message<?> message1) {
        super(message);
        this.message = message1;
    }

    public MessageExchangeStrategyNotFoundRuntimeException(String message, Throwable cause, Message<?> message1) {
        super(message, cause);
        this.message = message1;
    }

    public MessageExchangeStrategyNotFoundRuntimeException(Throwable cause, Message<?> message) {
        super(cause);
        this.message = message;
    }

    public MessageExchangeStrategyNotFoundRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Message<?> message1) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message1;
    }
}
