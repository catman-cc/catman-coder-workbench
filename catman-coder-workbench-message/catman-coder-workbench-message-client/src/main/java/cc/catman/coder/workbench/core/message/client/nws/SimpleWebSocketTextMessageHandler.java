package cc.catman.coder.workbench.core.message.client.nws;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * 一个比较简单的ws消息处理器
 */
public class SimpleWebSocketTextMessageHandler implements WebSocketTextMessageHandler{
    private ObjectMapper objectMapper;

    private IMessageExchange messageExchange;

    public SimpleWebSocketTextMessageHandler(ObjectMapper objectMapper, IMessageExchange messageExchange) {
        this.objectMapper = objectMapper;
        this.messageExchange = messageExchange;
    }

    @Override
    @SneakyThrows
    public void onMessage(String text, MessageConnection<?> connection) {
        // 1. 解析命令
        JsonTreeMessage message = this.objectMapper.readValue(text, JsonTreeMessage.class);
        this.messageExchange.exchange(message,connection);
    }
}
