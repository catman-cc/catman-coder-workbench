package cc.catman.coder.workbench.core.schedule;

import cc.catman.coder.workbench.core.executor.IExecutor;
import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;

/**
 * 调度器,负责调度任务,比如可以实现对任务的负载均衡,选择合理的运行节点等
 */
public interface ISchedule {

    /**
     * 调度任务,返回一个合适的执行器
     * @param provider 任务提供者
     * @return 执行器
     */
    IExecutor schedule(IFunctionRuntimeProvider provider);
}
