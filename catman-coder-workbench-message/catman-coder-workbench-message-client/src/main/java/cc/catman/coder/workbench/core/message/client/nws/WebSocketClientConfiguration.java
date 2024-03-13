package cc.catman.coder.workbench.core.message.client.nws;

import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

/**
 * WebSocket客户端配置
 */
@Data
@Builder
public class WebSocketClientConfiguration {

    /**
     * 服务器链接
     */
    private String url;

    private IMessageExchange messageExchange;

    private ObjectMapper objectMapper;

    private WebSocketTextMessageHandler textMessageHandler;
}
