package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.connection.AbstractMessageConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WebSocketMessageConnection extends AbstractMessageConnection<Session> {
    private ObjectMapper objectMapper;
    @Override
    public boolean isAlive() {
        return getRawConnection().isOpen();
    }
    @Override
    @SneakyThrows
    public MessageACK send(Message<?> message) {
        message.setSender(getId());
        Session session = this.getRawConnection();
        session.getAsyncRemote().sendObject(message);
//        session.getBasicRemote().sendObject(message);
        return MessageACK.ACK;
    }
}
