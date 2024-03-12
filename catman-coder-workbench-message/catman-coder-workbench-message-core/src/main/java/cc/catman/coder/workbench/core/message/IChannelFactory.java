package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.channel.DefaultChannelFactory;
import cc.catman.coder.workbench.core.message.channel.IChannelCreator;
import cc.catman.coder.workbench.core.message.system.CreateChannel;

public interface IChannelFactory {
    DefaultChannelFactory add(String channelKind, IChannelCreator channelCreator);

    MessageChannel createChannel(CreateChannel option, MessageConnection<?> connection, ChannelManager channelManager);
}
