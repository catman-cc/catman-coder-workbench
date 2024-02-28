package cc.catman.coder.workbench.core.node;

/**
 * 工作节点观察者,负责观察工作节点的状态,当节点状态发生变化时,会通知观察者
 * 比如: 注册节点,注销节点,节点状态变化等
 */
public interface IWorkerWatcher {

    IWorkerManager getWorkerManager();

    void setWorkerManager(IWorkerManager workerManager);

    default void onWorkerRegister(IWorker worker){

    }

    default void onWorkerUnregister(IWorker worker){

    }

    default void onWorkerStatusChange(IWorker worker){

    }
}
