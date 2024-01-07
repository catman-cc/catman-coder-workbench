package cc.catman.workbench.api.server.websocket;


import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.MessageConnectionManager;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import cc.catman.workbench.api.server.configuration.message.WebSocketMessageConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *  节点接入的websocket
 */
@ServerEndpoint("/websocket/node/{code}")
public class ExecutorWebSocketController {

    private MessageConnectionManager connectionManager;

    private ObjectMapper objectMapper;
    @OnOpen
    public void onOpen(Session session, @PathParam("code") String code) {
        // 当一个节点执行器接入时,需要将该节点的session保存起来,以便后续向该节点发送消息
        //
        WebSocketMessageConnection websocket = WebSocketMessageConnection.builder()
                .id(session.getId())
                .rawConnection(session)
                .type("websocket")
                .objectMapper(objectMapper)
                .build();
        connectionManager.addConnection(websocket);
    }

    @OnClose
    public void onClose() {
    }

    @OnMessage
    @SneakyThrows
    public void onMessage(Session session,String message) {
        MessageConnection<?> connection = connectionManager.getConnection(session.getId());
        JsonTreeMessage stringMessage = objectMapper.readValue(message, JsonTreeMessage.class);
        // 将消息发送给调度器
    }
    @OnError
    public void onError(Session session,Throwable error) {
    }
}
