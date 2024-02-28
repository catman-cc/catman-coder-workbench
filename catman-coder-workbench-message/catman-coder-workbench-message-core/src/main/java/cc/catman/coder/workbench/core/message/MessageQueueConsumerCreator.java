package cc.catman.coder.workbench.core.message;

import java.util.function.Consumer;

/**
 * 消息队列消费者创建器
 */
@FunctionalInterface
public interface MessageQueueConsumerCreator<T> {
    Consumer<T> create(String key,T message,GroupedMessageQueue<T> groupedMessageQueue);
}
