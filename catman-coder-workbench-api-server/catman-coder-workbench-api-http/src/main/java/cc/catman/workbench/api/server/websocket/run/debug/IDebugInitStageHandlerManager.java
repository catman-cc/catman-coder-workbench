package cc.catman.workbench.api.server.websocket.run.debug;

import java.util.List;

/**
 * 调试会话初始化阶段处理器管理器
 */
public interface IDebugInitStageHandlerManager {

    /**
     * 获取调试会话初始化阶段处理器
     * @param stage 阶段
     * @return 调试会话初始化阶段处理器
     */
    List<IDebugInitStageHandler> getDebugInitStageHandler(EDebugInitStage stage);
}
