package cc.catman.workbench.api.server.configuration.message.handlers;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.channel.DefaultChannelCreator;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import cc.catman.workbench.api.server.configuration.message.connection.WebSocketSessionConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Slf4j
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

        MessageConnection<?> connection = getOrCreateConnection(session);

        this.messageExchange.exchange(mes,connection);
    }

    private MessageConnection<?> getOrCreateConnection(WebSocketSession session) {
        return this.channelManager.getMessageConnectionManager()
                .getOrCreateConnection(
                        WebSocketSessionConnection.PREFIX + session.getId(),
                        id -> WebSocketSessionConnection.builder()
                                .id(id)
                                .objectMapper(objectMapper)
                                .rawConnection(session)
                                .build());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        session.sendMessage(new PongMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 首次创建链接,为链接创建一个默认的信道,该信道用于处理默认数据,比如心跳,系统消息等
        IChannelFactory channelFactory = this.channelManager.getChannelFactory();
        MessageConnection<?> connection = getOrCreateConnection(session);
        CreateChannelOptions options = CreateChannelOptions.builder()
                .channelId("default-" + connection.getId())
                .kind("default")
                .build();

        log.info("create default channel for connection {},from {}", connection.getId(),session.getRemoteAddress());
        this.channelManager.create(options,connection);
        super.afterConnectionEstablished(session);
    }
}
