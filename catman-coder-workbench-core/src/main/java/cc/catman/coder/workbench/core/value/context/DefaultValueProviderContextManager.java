package cc.catman.coder.workbench.core.value.context;

import cc.catman.coder.workbench.core.value.IValueProviderContextManager;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultValueProviderContextManager implements IValueProviderContextManager {

    @Builder.Default
    private Map<String, List<ValueProviderContext>> contexts=new ConcurrentHashMap<>();

    @Override
    public List<ValueProviderContext> getContexts(String batchId) {
        return contexts.get(batchId);
    }

    @Override
    public Optional<ValueProviderContext> getContext(String batchId, String contextId) {
        return Optional.ofNullable(contexts.get(batchId))
                .flatMap(l -> l.stream().filter(c -> c.getId().equals(contextId))
                        .findFirst());
    }

    @Override
    public void addContext(ValueProviderContext context) {
        contexts.computeIfAbsent(context.getBatchId(),k-> new ArrayList<>())
                        .add(context);
    }

    @Override
    public void removeContext(ValueProviderContext context) {
        contexts.get(context.getBatchId()).remove(context);
    }

    @Override
    public void updateContext(ValueProviderContext context) {
        contexts.get(context.getBatchId()).set(contexts.get(context.getBatchId()).indexOf(context),context);
    }

    @Override
    public void clearContexts(String batchId) {
        contexts.remove(batchId);
    }

    @Override
    public void clearAllContexts() {
        contexts.clear();
    }
}
