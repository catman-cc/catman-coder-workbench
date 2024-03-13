package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.channel.DefaultChannelFactory;
import cc.catman.coder.workbench.core.message.channel.IChannelCreator;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;

public interface IChannelFactory {
    DefaultChannelFactory add(String channelKind, IChannelCreator channelCreator);

    MessageChannel createChannel(CreateChannelOptions option, MessageConnection<?> connection, ChannelManager channelManager);
}
