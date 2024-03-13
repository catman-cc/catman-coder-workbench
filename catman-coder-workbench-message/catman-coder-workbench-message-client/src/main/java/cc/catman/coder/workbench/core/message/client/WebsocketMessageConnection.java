package cc.catman.coder.workbench.core.message.client;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.client.nws.WebSocketClient;
import cc.catman.coder.workbench.core.message.connection.AbstractMessageConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WebsocketMessageConnection extends AbstractMessageConnection<WebSocketClient> {

    @Override
    public boolean isAlive() {
        return this.getRawConnection().channel().isActive();
    }

    @Override
    public void close() {
        this.getRawConnection().channel().close().addListener(future -> {
            if (future.isSuccess()) {
                // 关闭成功
                log.info("close connection {} success", this.getId());
            } else {
                // 关闭失败
                log.error("close connection {} fail", this.getId());
            }
        });
    }

    @Override
    public MessageACK send(Message<?> message) {
        return this.getRawConnection().send(message);
    }
}
