package cc.catman.workbench.api.server.configuration.join;

public enum ExecutorStatus {
    /**
     * 空闲
     */
    IDLE,
    /**
     * 准备中
     */
    PREPARING,
    /**
     * 运行中
     */
    RUNNING,
    /**
     * 已停止
     */
    STOPPED,

    /**
     * 限流中
     */
    LIMITING,
    /**
     * 已销毁
     */
    DESTROYED,
    /**
     * 异常
     */
    EXCEPTION,
    /**
     * 未知
     */
    UNKNOWN,
}
