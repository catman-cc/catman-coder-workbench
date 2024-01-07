package cc.catman.workbench.api.server.websocket.run.debug;

import javax.websocket.Session;

public interface IDebugSession {

    /**
     * 获取调试会话id
     * @return 调试会话id
     */
    String getSessionId();

    Session getSession();

    void updateSession(Session session);

    /**
     * 启用调试会话
     */
    void  start();
}
