package cc.catman.workbench.api.server.websocket.run.debug.command;

import cc.catman.workbench.api.server.websocket.message.DebugCommand;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "command")
public class Command {
    protected DebugCommand command;
}
