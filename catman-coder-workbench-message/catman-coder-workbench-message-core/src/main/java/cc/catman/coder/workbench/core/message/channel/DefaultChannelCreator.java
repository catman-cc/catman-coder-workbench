package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

/**
 * 默认信道创建器
 */
@Slf4j
public class DefaultChannelCreator implements IChannelCreator{
    @Override
    public MessageChannel createChannel(CreateChannelOptions option, MessageConnection<?> connection, ChannelManager channelManager) {
        if (Optional.ofNullable(option.getKind()).isEmpty()) {
            option.setKind("default");
        }
        if (Optional.ofNullable(option.getChannelId()).isEmpty()) {
            option.setChannelId("default-" + connection.getId());
        }

        String wantChannelId = option.getChannelId();
        Optional<MessageChannel> channelOptional = channelManager.getChannel(wantChannelId);
        if (channelOptional.isPresent()) {
            // 已有链接
            MessageChannel channel = channelOptional.get();
            MessageConnection<?> mc = channel.getConnection();
            if(mc.isAlive()){
                // 已有链接并且是活跃的
                if (connection.equals(mc)) {
                    // 已有链接并且是活跃的,并且是同一个链接
                    channelManager.bindDefault(connection,channel);
                    return channel;
                } else {
                    // 已有链接并且是活跃的,但是不是同一个链接
                    log.error("channel {} already exist, but not same connection,so will replace it", wantChannelId);
                    mc.close();
                }
            }
        }
        DefaultMessageChannel newChannel = createMessageChannel(option, connection, channelManager);
        channelManager.bindDefault(connection,newChannel);
        return newChannel;
    }

    private static DefaultMessageChannel createMessageChannel(CreateChannelOptions option, MessageConnection<?> connection, ChannelManager channelManager) {
        return DefaultMessageChannel.builder()
                .channelManager(channelManager)
                .id(option.getChannelId())
                .connection(connection)
                .build();
    }

}
