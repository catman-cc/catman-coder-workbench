package cc.catman.workbench.api.server.websocket;

/**
 * 节点状态
 * - 未连接
 * - 已连接
 * - 已断开
 * - 已失效
 * - 正在重新连接
 * - 正在断开连接
 */
public enum ExecutorState {
    NOT_CONNECTED,
    CONNECTED,
    DISCONNECTED,
    INVALID,
    RECONNECTING,
    DISCONNECTING
}
