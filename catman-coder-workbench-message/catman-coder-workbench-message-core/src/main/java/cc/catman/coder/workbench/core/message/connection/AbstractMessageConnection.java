package cc.catman.coder.workbench.core.message.connection;

import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.MessageContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMessageConnection<T> implements MessageConnection<T>{
    private String id;
    private String type;
    private T rawConnection;

    private MessageContext context;
}
