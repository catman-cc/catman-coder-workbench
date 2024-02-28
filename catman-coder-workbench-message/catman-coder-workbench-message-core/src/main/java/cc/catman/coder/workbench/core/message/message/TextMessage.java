package cc.catman.coder.workbench.core.message.message;

import cc.catman.coder.workbench.core.message.Message;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("text")
public class TextMessage extends Message<String> {
}
