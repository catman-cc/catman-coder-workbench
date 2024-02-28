package cc.catman.coder.workbench.core.node;

import java.util.List;

/**
 * 工作节点管理器,一个工作节点可能提供多种能力
 * - 函数执行节点
 * - 函数调度节点
 * 注册节点的方式有多种,比如,将本地节点注册为工作节点
 */
public interface IWorkerManager {

    /**
     * 注册一个工作节点
     */
    void register(IWorker worker);

    void unregister(IWorker worker);

    /**
     * 获取所有的工作节点
     */
    List<IWorker> list();


    void watch(IWorkerWatcher watcher);

    void unwatch(IWorkerWatcher watcher);

}
