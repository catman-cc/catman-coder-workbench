package cc.catman.workbench.api.server.configuration.message.connection;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.connection.AbstractMessageConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.Session;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Data
@Slf4j
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebSocketMessageConnection extends AbstractMessageConnection<Session> {
    private ObjectMapper objectMapper;
    @Override
    public boolean isAlive() {
        return getRawConnection().isOpen();
    }

    @Override
    public void close() {
        try {
            this.getRawConnection().close();
            log.info("close connection {} success", this.getId());
        } catch (IOException e) {
            log.error("close connection {} fail,because: {}", this.getId(),e);
        }
    }

    @Override
    @SneakyThrows
    public MessageACK send(Message<?> message) {
        message.setSender(getId());
        Session session = this.getRawConnection();
        session.getAsyncRemote().sendObject(message);
        return MessageACK.ACK;
    }
}
