package cc.catman.workbench.api.server.websocket.run.debug.command;

/**
 * 调试会话命令
 */
public enum DebugCommand {
    /**
     * 更新断点
     */
    UPDATE_BREAKPOINT,
    /**
     * 更新变量
     */
    UPDATE_VARIABLE,
    /**
     * 更新ValueProviderDefinition
     */
    UPDATE_VALUE_PROVIDER_DEFINITION,
    /**
     * 启动调试会话
     */
    START,
    /**
     * 暂停调试会话
     */
    PAUSE,
    /**
     * 恢复调试会话
     */
    RESUME,
    /**
     * 停止调试会话
     */
    STOP,
    /**
     *  单步调试
     */
    STEP_INTO,
    /**
     * 单步调试
     */
    STEP_OVER,
    /**
     * 单步调试
     */
    STEP_OUT,
    ;
}
