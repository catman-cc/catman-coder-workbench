package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultChannelFactory implements IChannelFactory {

    @Builder.Default
    private Map<String, IChannelCreator> channelCreatorMap = new ConcurrentHashMap<>();

    @Override
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
                    return channel;
                })
                .orElseThrow();
    }
}
