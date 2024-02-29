package cc.catman.workbench.api.server.configuration.message.dto;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.Command;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("command")
public class CommandMessage extends Message<Command> {
}
