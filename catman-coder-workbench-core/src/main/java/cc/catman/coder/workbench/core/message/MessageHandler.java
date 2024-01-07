package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import com.fasterxml.jackson.core.type.TypeReference;

public interface MessageHandler<T> {
    default TypeReference<T> supportType(){
        return new TypeReference<T>() {
        };
    }

    /**
     * 处理消息
     *
     * @param message 消息
     */
    MessageACK handle(Message<T> message);

    Message<T> createFormatMessage(JsonTreeMessage message);
}
