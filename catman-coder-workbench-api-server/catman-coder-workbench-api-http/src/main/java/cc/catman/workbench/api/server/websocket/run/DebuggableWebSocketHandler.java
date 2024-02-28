package cc.catman.workbench.api.server.websocket.run;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import cc.catman.workbench.api.server.configuration.message.WebSocketSessionConnection;
import cc.catman.workbench.api.server.websocket.run.debug.IDebugSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import jakarta.annotation.Resource;

@Component
public class DebuggableWebSocketHandler extends AbstractWebSocketHandler {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private MessageConnectionManager messageConnectionManager;

    @Resource
    private MessageExchange messageExchange;
    @Resource
    private IDebugSessionManager debugSessionManager;

    @Resource
    private HttpMessageConverters httpMessageConverters;

    @Resource
    private ChannelManager channelManager;

    @Override
    @SneakyThrows
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        String content = message.getPayload();
        // 1. 解析命令
        JsonTreeMessage mes=objectMapper.readValue(content, JsonTreeMessage.class);

        MessageConnection<?> connection = messageConnectionManager.getOrCreateConnection(session.getId(),
                id -> WebSocketSessionConnection.builder()
                        .id(id)
                        .objectMapper(objectMapper)
                        .rawConnection(session)
                        .build());

        // 调用ChannelManager获取Channel,并将Channel设置到Message中
        MessageChannel messageChannel = channelManager.getOrCreateChannel(mes, connection);
        mes.setMessageChannel(messageChannel);
        // 2. 解析命令
        MessageACK ack = messageExchange.route(mes);
        // nothing
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
       session.sendMessage(new PongMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        MessageConnection<?> connection = messageConnectionManager.getOrCreateConnection(session.getId(), id -> WebSocketSessionConnection.builder()
                .id(id)
                .objectMapper(objectMapper)
                .rawConnection(session)
                .build());
        super.afterConnectionEstablished(session);
    }
}
