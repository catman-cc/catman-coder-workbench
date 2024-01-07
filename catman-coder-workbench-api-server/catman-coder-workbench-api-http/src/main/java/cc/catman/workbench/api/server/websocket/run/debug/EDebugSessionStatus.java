package cc.catman.workbench.api.server.websocket.run.debug;

/**
 * 调试会话状态
 */
public enum EDebugSessionStatus {
    /**
     * 等待启动
     */
    WAITING_FOR_START,
    /**
     * 调试会话已经启动
     */
    STARTED,
    /**
     * 调试会话已经停止
     */
    STOPPED,
    /**
     * 调试会话已经暂停
     */
    PAUSED,
    /**
     * 调试会话已经恢复
     */
    RESUMED,
    /**
     * 调试会话已经结束
     */
    FINISHED,
}
