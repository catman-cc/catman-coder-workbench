package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import lombok.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 这里作为一个简单实现,一个channel只能绑定一个session
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultChannelManager implements ChannelManager {


    private MessageConnectionManager messageConnectionManager;

    private IChannelFactory channelFactory;

    @Builder.Default
    private List<MessageChannel> messageChannels =new CopyOnWriteArrayList<>();
    @Builder.Default
    private Map<String, String> channelToSession=new ConcurrentHashMap<>();

    @Override
    public Optional<MessageChannel> getChannel(String id) {
        return  messageChannels.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public MessageConnection<?> findBindConnection(String channelId){
      return Optional.ofNullable( this.channelToSession.get(channelId))
              .map(s -> this.messageConnectionManager.getConnection(s)).orElseThrow();
    }

    @Override
    public boolean flushChannel(MessageChannel messageChannel, MessageConnection<?> connection) {
            if (Optional.ofNullable(messageChannel).isEmpty()){
                return  false;
            }
            if (Optional.ofNullable(connection).isEmpty()){
                return false;
            }
            if (messageChannel.getConnection().equals(connection)){
                return true;
            }
            this.channelToSession.put(messageChannel.getId(),connection.getId());
            return true;

    }

    @Override
    public MessageChannel getOrCreateChannel(Message<?> message, MessageConnection<?> connection) {
        // 判断当前是否存在channel,如果存在则获取channel
        if (Optional.ofNullable(message.getChannelId()).isEmpty()){
            // 如果消息中没有channelId,则创建一个channelId
            message.setChannelId(UUID.randomUUID().toString());
        }
        String cid =message.getChannelId();
        MessageChannel messageChannel = getChannel(cid).orElseGet(() -> createChannel(message, connection));
        if(flushChannel(messageChannel,connection)){
            return messageChannel;
        }
        throw new RuntimeException("can not bind channel to session",new Throwable("channelId:"+cid+" connectionId:"+connection.getId()));
    }


    protected MessageChannel createChannel(Message<?> message, MessageConnection<?> connection){
        MessageChannel messageChannel = channelFactory.createChannel(message, connection,this);
        this.channelToSession.put(messageChannel.getId(),connection.getId());
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
