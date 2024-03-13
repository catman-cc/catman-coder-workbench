package cc.catman.workbench.api.server.configuration.message.connection;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.connection.AbstractMessageConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@Slf4j
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WebSocketSessionConnection extends AbstractMessageConnection<WebSocketSession> {

    public static final String PREFIX="websocket-";
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
        WebSocketSession con = getRawConnection();
        String msg = objectMapper.writeValueAsString(message);
        log.info("send message to client:{}", msg);
        con.sendMessage(new TextMessage(msg));
        log.info("send message to client:{}", msg);
        return MessageACK.ACK;
    }
}
