package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 消息信道管理器
 */
public interface ChannelManager {

    IChannelFactory getChannelFactory();
    Optional<MessageChannel> getChannel(String id);

    MessageConnectionManager getMessageConnectionManager();

    MessageChannel getOrCreateChannel(Message<?> message, MessageConnection<?> connection);

    MessageChannel getOrCreateChannel(Message<?> message, Supplier<MessageConnection<?>> connectionSupplier);

    MessageChannel create(CreateChannelOptions option, MessageConnection<?> connection);

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

    /**
     * 为一个链接绑定默认的信道
     * @param connection 连接
     * @param messageChannel 信道
     */
    void bindDefault(MessageConnection<?> connection,MessageChannel messageChannel);
}
