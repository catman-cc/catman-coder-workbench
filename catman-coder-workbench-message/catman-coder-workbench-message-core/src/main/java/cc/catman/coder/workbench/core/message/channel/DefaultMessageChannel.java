package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMessageChannel implements MessageChannel {
    protected String id;

    protected MessageConnection<?> connection;

    protected ChannelManager channelManager;
    @Override
    public MessageConnection<?> getConnection() {
        return this.channelManager.findBindConnection(this.id);
    }

    @Override
    public void onMessage(Message<?> message, MessageContext context) {

    }
    @Override
    public MessageACK send(Message<?> message) {
        if (Optional.ofNullable(message.getChannelId()).isEmpty()){
            message.setChannelId(this.id);
        }
        if (Optional.ofNullable(message.getChannelKind()).isEmpty()){
            message.setChannelKind("default");
        }
        return this.connection.send(message);
    }

    @Override
    public void send(Message<?> message, MessageHandlerCallback<?> callback) {
        callback.callback(message,this.send(message));
    }
}
