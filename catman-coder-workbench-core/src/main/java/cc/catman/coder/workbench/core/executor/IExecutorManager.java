package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.label.ISelector;

import java.util.List;

/**
 * 执行器管理器
 */
public interface IExecutorManager {
    List<IExecutor> listExecutors();

    IExecutor getExecutor(String id);

    List<IExecutor> find(ISelector selector);

    IExecutorManager addExecutor(IExecutor executor);

    IExecutorManager removeExecutor(String id);

    default IExecutorManager removeExecutor(IExecutor executor){
        return removeExecutor(executor.getId());
    }
}
