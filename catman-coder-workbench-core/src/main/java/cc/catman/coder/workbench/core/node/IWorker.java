package cc.catman.coder.workbench.core.node;

import cc.catman.coder.workbench.core.message.MessageBus;

import java.util.List;

/**
 * 工作节点,根据节点类型的不同,可能提供不同的能力
 */
public interface IWorker {

    String getId();
    /**
     * 获取工作节点的能力,比如:Scheduler,Executor等等
     */
    List<String> getCapabilities();


    /**
     * 判断工作节点是否具有某种能力
     */
    boolean hasCapability(String capability);

    /**
     * 判断工作节点是否是本地节点
     */
    boolean isLocal();
    /**
     * 获取工作节点的系统信息
     */
    WorkSystemInfo geSystemInfo();

    /**
     * 获取所属的工作节点管理器
     */
    IWorkerManager getWorkerManager();

    MessageBus getMessageBus();
    /**
     * 获取执行器服务,如果工作节点不具有执行器能力,则返回null
     */
    IExecutorService getExecutorService();
}
