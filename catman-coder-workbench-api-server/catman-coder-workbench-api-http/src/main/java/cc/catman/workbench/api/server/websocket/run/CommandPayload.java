package cc.catman.workbench.api.server.websocket.run;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, property = "command")
public interface CommandPayload {
}
