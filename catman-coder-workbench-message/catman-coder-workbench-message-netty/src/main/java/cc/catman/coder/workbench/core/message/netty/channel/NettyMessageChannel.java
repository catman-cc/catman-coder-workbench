package cc.catman.coder.workbench.core.message.netty.channel;

import cc.catman.coder.workbench.core.message.*;

public class NettyMessageChannel implements MessageChannel {

    private MessageConnection<?> connection;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public MessageConnection<?> getConnection() {
        return this.connection;
    }

    @Override
    public void onMessage(Message<?> message, MessageContext context) {

    }

    @Override
    public MessageACK send(Message<?> message) {
        return null;
    }

    @Override
    public void send(Message<?> message, MessageHandlerCallback<?> callback) {

    }
}
