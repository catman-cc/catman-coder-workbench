package cc.catman.workbench.api.server.websocket.run.debug;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultDebugInitStageHandlerManager implements IDebugInitStageHandlerManager{

    @Resource
    private List<IDebugInitStageHandler> debugInitStageHandlers;

    @Override
    public List<IDebugInitStageHandler> getDebugInitStageHandler(EDebugInitStage stage) {
        return debugInitStageHandlers.stream().filter(handler->handler.support(stage)).collect(Collectors.toList());
    }
}
