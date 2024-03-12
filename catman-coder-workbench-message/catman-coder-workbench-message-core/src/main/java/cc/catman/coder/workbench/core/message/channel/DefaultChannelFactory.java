package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.system.CreateChannel;
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
    public MessageChannel createChannel(CreateChannel option, MessageConnection<?> connection, ChannelManager channelManager) {
        String channelKind = Optional.ofNullable(option.getKind()).orElse("default");
        return Optional.ofNullable(channelCreatorMap.get(channelKind))
                .map(c -> c.createChannel(option, connection, channelManager))
                .orElseThrow();
    }
}
