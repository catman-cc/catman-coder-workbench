package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.channel.DefaultChannelFactory;
import cc.catman.coder.workbench.core.message.channel.IChannelCreator;

public interface IChannelFactory {
    DefaultChannelFactory add(String channelKind, IChannelCreator channelCreator);

    MessageChannel createChannel(Message<?> message, MessageConnection<?> connection, ChannelManager channelManager);
}
