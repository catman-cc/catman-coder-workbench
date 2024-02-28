package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageConnection;

@FunctionalInterface
public interface IChannelCreator {
    MessageChannel createChannel(Message<?> message, MessageConnection<?> connection, ChannelManager channelManager);
}
