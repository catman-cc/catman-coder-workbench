package cc.catman.workbench.api.server.websocket.run.debug;

/**
 * 调试会话初始化阶段处理器
 */
public interface IDebugInitStageHandler {
    /**
     * 是否支持该阶段
     * @param stage 阶段
     * @return 是否支持
     */
    boolean support(EDebugInitStage stage);

    /**
     * 处理调试会话初始化阶段
     * @param session 调试会话
     * @param stage 阶段
     */
    void handle(IDebugSession session, EDebugInitStage stage);
}
