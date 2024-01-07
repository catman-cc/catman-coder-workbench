package cc.catman.coder.workbench.core.message;

import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultMessageConnectionManager implements MessageConnectionManager{
    @Builder.Default
    private Map<String,MessageConnection<?>> connections=new HashMap<>();
    @Override
    public MessageConnection<?> getConnection(String id) {
        return connections.get(id);
    }

    @Override
    public MessageConnection<?> getOrCreateConnection(String id, Function<String, MessageConnection<?>> function) {
        return connections.computeIfAbsent(id,function);
    }

    @Override
    public void addConnection(MessageConnection<?> connection) {
        connections.put(connection.getId(),connection);
    }

    @Override
    public void removeConnection(String id) {
        connections.remove(id);
    }

    @Override
    public void removeConnection(MessageConnection<?> connection) {
        connections.remove(connection.getId());
    }
}
