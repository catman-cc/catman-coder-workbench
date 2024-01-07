package cc.catman.coder.workbench.core.message.connection;

import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.MessageContext;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Optional;


@Data
@SuperBuilder
public abstract class AbstractMessageConnection<T> implements MessageConnection<T>{
    private String id;
    private String type;
    private T rawConnection;
    private MessageContext context;
}
