package cc.catman.coder.workbench.core.message;

import java.util.Optional;

/**
 * 消息信道管理器
 */
public interface ChannelManager {
    Optional<MessageChannel> getChannel(String id);

    MessageChannel getOrCreateChannel(Message<?> message, MessageConnection<?> connection);

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
