package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class DefaultChannelFactory implements IChannelFactory {

    @Builder.Default
    private Map<String, IChannelCreator> channelCreatorMap = new ConcurrentHashMap<>();

    public DefaultChannelFactory add(String channelKind, IChannelCreator channelCreator) {
        channelCreatorMap.put(channelKind, channelCreator);
        return this;
    }

    @Override
    public MessageChannel createChannel(Message<?> message, MessageConnection<?> connection, ChannelManager channelManager) {
        String channelKind = Optional.ofNullable(message.getChannelKind()).orElse("default");
        return Optional.ofNullable(channelCreatorMap.get(channelKind))
                .map(c ->{
                    MessageChannel channel = c.createChannel(message, connection, channelManager);
                    message.setMessageChannel(channel);
                    message.answer(Message.of(Map.of("channelId", channel.getId(), "channelKind", channelKind)));
                    return channel;
                })
                .orElseThrow();
    }
}
