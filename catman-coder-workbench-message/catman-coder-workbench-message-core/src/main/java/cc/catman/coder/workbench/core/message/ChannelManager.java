package cc.catman.coder.workbench.core.message;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 消息信道管理器
 */
public interface ChannelManager {
    Optional<MessageChannel> getChannel(String id);

    MessageConnectionManager getMessageConnectionManager();

    MessageChannel getOrCreateChannel(Message<?> message, MessageConnection<?> connection);

    MessageChannel getOrCreateChannel(Message<?> message, Supplier<MessageConnection<?>> connectionSupplier);

    MessageConnection<?> findBindConnection(String channelId);

    boolean flushChannel(MessageChannel messageChannel, MessageConnection<?> connection);
    default  boolean flushChannel(String channelId, MessageConnection<?> connection){
        return Optional.ofNullable(channelId)
                .map(this::getChannel)
                .map(c -> flushChannel(c.orElse(null),connection))
                .orElse(false);
    }

    void addChannel(MessageChannel messageChannel);

    void removeChannel(String id);

    void removeChannel(MessageChannel messageChannel);
}
