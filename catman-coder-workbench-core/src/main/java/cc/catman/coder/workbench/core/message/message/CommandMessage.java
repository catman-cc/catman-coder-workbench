package cc.catman.coder.workbench.core.message.message;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.Command;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("command")
public class CommandMessage extends Message<Command> {
}
