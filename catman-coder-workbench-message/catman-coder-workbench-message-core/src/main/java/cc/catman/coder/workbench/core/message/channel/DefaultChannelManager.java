package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import lombok.*;
import org.codehaus.commons.nullanalysis.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    /**
     * 用于记录默认的连接通道
     */
    @Builder.Default
    private Map<String,String> defaultConnectionChannel=new ConcurrentHashMap<>();

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

    @Override
    public MessageChannel create(CreateChannelOptions option, MessageConnection<?> connection) {
        if (Optional.ofNullable(option).isEmpty()) {
            throw new RuntimeException("create channel option is null");
        }
        if (Optional.ofNullable(option.getChannelId()).isEmpty()) {
            throw new RuntimeException("create channel option id is null");
        }

        MessageChannel messageChannel = channelFactory.createChannel(option, connection, this);
        if (messageChannel == null) {
            return null;
        }
        this.channelToSession.put(messageChannel.getId(), connection.getId());
        this.messageChannels.add(messageChannel);
        return messageChannel;
    }

    @Nullable
    private MessageChannel getMessageChannel(Message<?> message, String cid, MessageConnection<?> messageConnection) {
        // 处理使用默认通道的消息,默认通道用于处理系统消息,比如,创建通道,关闭通道,心跳等
        if(isSystemChannel(message,messageConnection)){
            String defaultChannel = this.defaultConnectionChannel.get(messageConnection.getId());
            Optional<MessageChannel> channelOptional = this.getChannel(defaultChannel);
            if(channelOptional.isPresent()){
                MessageChannel channel = channelOptional.get();
                message.setMessageChannel(channel);
                if (flushChannel(channel, messageConnection)) {
                    return channel;
                }
                return channel;
            }
            // 理论上,在初次链接的时候,会创建一个默认通道,如果此处无法获取到默认通道,则说明通道创建失败,那么就需要抛出异常
            throw new RuntimeException("can not find default channel", new Throwable("channelId:" + defaultChannel + " connectionId:" + messageConnection.getId()));
        }
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

    private boolean isSystemChannel(Message<?> message,MessageConnection<?> connection){
        return Constants.Message.Channel.SYSTEM_ID.equals(message.getChannelId())
                || Constants.Message.Channel.SYSTEM_KIND.equals(message.getChannelKind());
    }

    protected MessageChannel createChannel(Message<?> message, MessageConnection<?> connection) {
        MessageChannel messageChannel = channelFactory.createChannel( CreateChannelOptions.builder().channelId(message.getChannelId()).kind(message.getChannelKind()).build(), connection, this);
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

    @Override
    public void bindDefault(MessageConnection<?> connection, MessageChannel messageChannel) {
        this.defaultConnectionChannel.put(connection.getId(),messageChannel.getId());
    }
}
