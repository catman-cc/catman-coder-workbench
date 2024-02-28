package cc.catman.workbench.api.server.websocket.run.debug;

import jakarta.websocket.Session;

public interface IDebugSessionManager {

    /**
     * 获取或创建调试会话
     * @param debugSessionId 调试会话id,如果没有传入debugSessionId,则会创建一个新的调试会话
     * @param session websocket会话,用于向客户端发送消息,以及接收客户端消息,如果传入了session,则会将session与调试会话绑定
     * @return 调试会话
     */
    IDebugSession getOrCreateDebugSession(String debugSessionId, Session session);
}
