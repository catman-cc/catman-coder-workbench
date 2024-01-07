package cc.catman.workbench.api.server.websocket.run.debug;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultDebugSession implements IDebugSession{
    private String sessionId;
    private Session session;

    private IDebugInitStageHandlerManager debugInitStageHandlerManager;
    @Override
    public String getSessionId() {
        return this.sessionId;
    }

    @Override
    public void updateSession(Session session) {
        this.session=session;
    }

    @Override
    public void start() {
        // 完成初始化阶段
        Arrays.asList(EDebugInitStage.values()).forEach(stage -> {
            debugInitStageHandlerManager.getDebugInitStageHandler(stage).forEach(handler -> {
                if(handler.support(stage)){
                    handler.handle(this, stage);
                }
            });
        });
    }
}
