package cc.catman.workbench.api.server.websocket.message;

public class ValueProviderExecMessage {

    /**
     * 调试会话id
     */
    private String debugSessionId;



    /**
     * 是否启用debug模式
     */
    private boolean isDebug;

    private boolean disableAllBreakpoint;

    private boolean disableAllWatchpoint;

    private boolean disableAllExceptionPoint;

}
