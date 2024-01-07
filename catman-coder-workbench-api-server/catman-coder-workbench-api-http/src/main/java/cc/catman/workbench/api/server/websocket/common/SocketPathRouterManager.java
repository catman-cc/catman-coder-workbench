package cc.catman.workbench.api.server.websocket.common;

import org.springframework.web.socket.WebSocketHandler;

/**
 * 为websocket创建一个路由管理器
 */
public interface SocketPathRouterManager {
    /**
     * 注册一个websocket处理器
     * @param socketHandler websocket处理器
     *                      一个处理器可以处理多个路径
     * @param path 路径
     */
    void register(WebSocketHandler socketHandler, String... path);

}
