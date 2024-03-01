package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import lombok.*;
import org.codehaus.commons.nullanalysis.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;

/**
 * 这里作为一个简单实现,一个channel只能绑定一个session
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultChannelManager implements ChannelManager {
    @Builder.Default
    private MessageConnectionManager messageConnectionManager=new DefaultMessageConnectionManager();
    @Builder.Default
    private IChannelFactory channelFactory= new DefaultChannelFactory();

    @Builder.Default
    private Set<MessageChannel> messageChannels = new CopyOnWriteArraySet<>();
    @Builder.Default
    private Map<String, String> channelToSession = new ConcurrentHashMap<>();

    @Override
    public Optional<MessageChannel> getChannel(String id) {
        return Optional.ofNullable(id).flatMap(cid->messageChannels.stream().filter(c -> c.getId().equals(cid)).findFirst());
    }

    public MessageConnection<?> findBindConnection(String channelId) {
        return Optional.ofNullable(this.channelToSession.get(channelId))
                .map(s -> this.messageConnectionManager.getConnection(s)).orElse(null);
    }

    @Override
    public boolean flushChannel(MessageChannel messageChannel, MessageConnection<?> connection) {
        if (Optional.ofNullable(messageChannel).isEmpty()) {
            return false;
        }
        if (Optional.ofNullable(connection).isEmpty()) {
            return false;
        }
        if (connection.equals(messageChannel.getConnection())) {
            return true;
        }
        this.channelToSession.put(messageChannel.getId(), connection.getId());
        this.messageConnectionManager.addConnection(connection);
        return true;

    }

    @Override
    public MessageChannel getOrCreateChannel(Message<?> message, MessageConnection<?> connection) {
        return getMessageChannel(message,  message.getChannelId(), connection);
    }

    @Override
    public MessageChannel getOrCreateChannel(Message<?> message, Supplier<MessageConnection<?>> connectionSupplier) {
        MessageConnection<?> messageConnection = connectionSupplier.get();
        return getMessageChannel(message,  message.getChannelId(), messageConnection);
    }

    @Nullable
    private MessageChannel getMessageChannel(Message<?> message, String cid, MessageConnection<?> messageConnection) {
        if (Optional.ofNullable(cid).isPresent()){
            return getChannel(cid)
                    .orElseThrow(()->new RuntimeException("can not find channel", new Throwable("channelId:" + cid + " connectionId:" + messageConnection.getId())));
        }

        MessageChannel messageChannel = createChannel(message, messageConnection);
        if (messageChannel==null){
            throw new RuntimeException("can not create channel", new Throwable("channelId:" + cid + " connectionId:" + messageConnection.getId()));
        }
        message.setMessageChannel(messageChannel);
        if (flushChannel(messageChannel, messageConnection)) {
            return messageChannel;
        }
        throw new RuntimeException("can not bind channel to session", new Throwable("channelId:" + cid + " connectionId:" + messageConnection.getId()));
    }


    protected MessageChannel createChannel(Message<?> message, MessageConnection<?> connection) {
        MessageChannel messageChannel = channelFactory.createChannel(message, connection, this);
        if (messageChannel == null) {
            return null;
        }
        this.channelToSession.put(messageChannel.getId(), connection.getId());
        this.messageChannels.add(messageChannel);
        return messageChannel;
    }

    @Override
    public void addChannel(MessageChannel messageChannel) {
        this.messageChannels.add(messageChannel);
    }

    @Override
    public void removeChannel(String id) {
        getChannel(id).ifPresent(this::removeChannel);
    }

    @Override
    public void removeChannel(MessageChannel messageChannel) {
        // 其实可以通过channel的id来删除
        this.messageChannels.remove(messageChannel);
    }
}
