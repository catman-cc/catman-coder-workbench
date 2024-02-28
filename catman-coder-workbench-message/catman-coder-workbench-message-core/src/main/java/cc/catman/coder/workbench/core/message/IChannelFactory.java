package cc.catman.coder.workbench.core.message;

public interface IChannelFactory {
    MessageChannel createChannel(Message<?> message, MessageConnection<?> connection, ChannelManager channelManager);
}
