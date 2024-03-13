package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;

@FunctionalInterface
public interface IChannelCreator {
    MessageChannel createChannel(CreateChannelOptions option, MessageConnection<?> connection, ChannelManager channelManager);
}
