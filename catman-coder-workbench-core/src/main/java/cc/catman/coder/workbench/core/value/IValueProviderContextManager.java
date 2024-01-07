package cc.catman.coder.workbench.core.value;

import java.util.List;
import java.util.Optional;

/**
 * 值提供者上下文管理器,用于管理值提供者上下文
 */
public interface IValueProviderContextManager {
    List<ValueProviderContext> getContexts(String batchId);

    Optional<ValueProviderContext> getContext(String batchId, String contextId);

    void addContext(ValueProviderContext context);

    void removeContext(ValueProviderContext context);

    void updateContext(ValueProviderContext context);

    void clearContexts(String batchId);

    void clearAllContexts();

}
