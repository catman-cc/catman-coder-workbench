package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.workbench.api.server.configuration.message.handlers.WebSocketMessageHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class WebsocketMessageConfiguration implements WebSocketConfigurer {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ChannelManager channelManager;
    @Resource
    private IMessageExchange messageExchange;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( new WebSocketMessageHandler(objectMapper, messageExchange, channelManager), "/message")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }
}
