package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.system.CreateChannel;

@FunctionalInterface
public interface IChannelCreator {
    MessageChannel createChannel(CreateChannel option, MessageConnection<?> connection, ChannelManager channelManager);
}
