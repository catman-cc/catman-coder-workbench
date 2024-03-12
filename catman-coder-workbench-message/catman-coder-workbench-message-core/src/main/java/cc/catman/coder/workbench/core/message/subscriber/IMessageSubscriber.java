package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.MessageType;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IMessageSubscriber extends Closeable {

    /**
     * 支持的消息类型,默认支持所有消息类型
     * @return 消息类型
     */
    default List<MessageType> supportMessageTypes() {
        return List.of(MessageType.values());
    }

    /**
     * 订阅者属性
     * @return 属性
     */
    default Map<String,Object> attributes(){
        return new HashMap<>();
    }

    default <T> T getAttr(String key){
        return (T) attributes().get(key);
    }

    /**
     * 是否匹配消息
     *
     * @param message 消息
     * @return 是否匹配
     */
    boolean isMatch(Message<?> message);

    /**
     * 接收消息,返回是否接收成功
     *
     * @param message 消息
     */
    MessageResult onReceive(Message<?> message);

    @Override
    default void close() throws IOException{

    }
}
