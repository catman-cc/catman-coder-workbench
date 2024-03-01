package cc.catman.workbench.api.server.configuration.message.handlers;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import cc.catman.workbench.api.server.configuration.message.connection.WebSocketSessionConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class WebSocketMessageHandler extends AbstractWebSocketHandler {
    private ObjectMapper objectMapper;
    private IMessageExchange messageExchange;

    private ChannelManager channelManager;

    public WebSocketMessageHandler(ObjectMapper objectMapper, IMessageExchange messageExchange, ChannelManager channelManager) {
        this.objectMapper = objectMapper;
        this.messageExchange = messageExchange;
        this.channelManager = channelManager;
    }

    @Override
    @SneakyThrows
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        String content = message.getPayload();
        // 1. 解析命令
        JsonTreeMessage mes = objectMapper.readValue(content, JsonTreeMessage.class);

        MessageConnection<?> connection = this.channelManager.getMessageConnectionManager()
                .getOrCreateConnection(
                        WebSocketSessionConnection.PREFIX + session.getId(),
                        id -> WebSocketSessionConnection.builder()
                                .id(id)
                                .objectMapper(objectMapper)
                                .rawConnection(session)
                                .build());

        this.messageExchange.exchange(mes,connection);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        session.sendMessage(new PongMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }
}
