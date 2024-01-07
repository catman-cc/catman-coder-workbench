package cc.catman.workbench.api.server.websocket.run.debug;

/**
 * 调试会话初始化阶段
 */
public enum EDebugInitStage {
    /**
     * 交换ValueProviderDefinition信息
     */
    EXCHANGE_VALUE_PROVIDER_DEFINITION,
    /**
     * 交换breakpoint信息
     */
    EXCHANGE_BREAKPOINT(true),

    /**
     * 交换变量信息
     */
    EXCHANGE_VARIABLE,
    ;
    /**
     * 是否只在debug模式下生效
     */
    final private boolean onlyDebug;

    EDebugInitStage() {
        this.onlyDebug = true;
    }

    EDebugInitStage(boolean onlyDebug) {
        this.onlyDebug = onlyDebug;
    }

    public boolean isOnlyDebug() {
        return onlyDebug;
    }

}
