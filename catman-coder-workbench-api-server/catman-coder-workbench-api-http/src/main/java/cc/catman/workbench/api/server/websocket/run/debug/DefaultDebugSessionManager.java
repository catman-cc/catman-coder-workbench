package cc.catman.workbench.api.server.websocket.run.debug;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultDebugSessionManager implements IDebugSessionManager {
    private Map<String,IDebugSession> sessions=new ConcurrentHashMap<>();

    @Resource
    private IDebugInitStageHandlerManager debugInitStageHandlerManager;


    @Override
    public IDebugSession getOrCreateDebugSession(String debugSessionId, Session session) {
        IDebugSession debugSession = sessions.computeIfAbsent(debugSessionId, id -> createDebugSession(debugSessionId, session));
        if (!session.getId().equals(debugSession.getSession().getId())){
            debugSession.updateSession(session);
        }
        return debugSession;
    }

    protected IDebugSession createDebugSession(String debugSessionId, Session session){
        return DefaultDebugSession.builder()
                .sessionId(UUID.randomUUID().toString())
                .debugInitStageHandlerManager(debugInitStageHandlerManager)
                .session(session)
                .build();
    }
}
