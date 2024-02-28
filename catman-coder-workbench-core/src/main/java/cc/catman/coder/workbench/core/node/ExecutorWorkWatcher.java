package cc.catman.coder.workbench.core.node;

import cc.catman.coder.workbench.core.executor.IExecutorManager;
import cc.catman.coder.workbench.core.executor.WorkerExecutor;

/**
 * 执行器节点观察者
 */
public class ExecutorWorkWatcher implements IWorkerWatcher{
    protected IExecutorManager executorManager;

    protected IWorkerManager workerManager;

    public ExecutorWorkWatcher(IExecutorManager executorManager) {
        this.executorManager = executorManager;
    }

    @Override
    public IWorkerManager getWorkerManager() {
        return this.workerManager;
    }

    @Override
    public void setWorkerManager(IWorkerManager workerManager) {
        this.workerManager = workerManager;
    }

    @Override
    public void onWorkerRegister(IWorker worker) {
        if (!worker.hasCapability("Executor")) {
            return;
        }
        executorManager.addExecutor(WorkerExecutor.builder()
                .id(worker.getId())
                .worker(worker)
                .build());
    }

    @Override
    public void onWorkerUnregister(IWorker worker) {
        executorManager.removeExecutor(worker.getId());
    }
}
