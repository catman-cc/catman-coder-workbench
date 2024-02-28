package cc.catman.workbench.api.server.websocket.run.debug.stage;

import cc.catman.workbench.api.server.websocket.run.debug.EDebugInitStage;
import cc.catman.workbench.api.server.websocket.run.debug.IDebugInitStageHandler;
import cc.catman.workbench.api.server.websocket.run.debug.IDebugSession;
import org.springframework.stereotype.Component;

import jakarta.websocket.Session;

/**
 * 交换ValueProviderDefinition信息调试会话初始化阶段处理器
 */
@Component
public class ValueProviderDefinitionDebugInitStageHandler implements IDebugInitStageHandler {
    @Override
    public boolean support(EDebugInitStage stage) {
        return EDebugInitStage.EXCHANGE_VALUE_PROVIDER_DEFINITION.equals(stage);
    }

    @Override
    public void handle(IDebugSession session, EDebugInitStage stage) {
        Session ws = session.getSession();
        ws.getAsyncRemote().sendObject("hello");
    }
}
