package cc.catman.workbench.api.server.websocket.message;

/**
 *  调试命令
 */
public enum DebugCommand {
    STEP_INTO("stepInto"),
    STEP_OVER("stepOver"),
    STEP_OUT("stepOut"),
    RESUME("resume"),
    PAUSE("pause"),
    STOP("stop"),
    UPDATE_BREAKPOINT("updateBreakpoint"),
    UPDATE_VARIABLE("updateVariable"),
    UPDATE_VALUE_PROVIDER_DEFINITION("updateValueProviderDefinition"),

    ;
    private String value;

    DebugCommand(String value) {
        this.value = value;
    }
}
