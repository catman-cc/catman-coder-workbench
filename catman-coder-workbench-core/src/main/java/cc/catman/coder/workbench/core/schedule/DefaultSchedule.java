package cc.catman.coder.workbench.core.schedule;

import cc.catman.coder.workbench.core.executor.IExecutor;
import cc.catman.coder.workbench.core.executor.IExecutorManager;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;

import java.util.List;

/**
 * 默认调度器,调度器负责调度任务,为任务选择合适的执行器
 * 默认的调度器会
 */
public class DefaultSchedule implements ISchedule{
    private IExecutorManager executorManager;

    public DefaultSchedule(IExecutorManager executorManager) {
        this.executorManager = executorManager;
    }

    @Override
    public IExecutor schedule(IFunctionRuntimeProvider provider) {
        // 根据kind选择合适的执行器
        IFunctionInfo functionInfo = provider.getFunctionInfo().getFunctionInfo();
        List<IExecutor> candidate = executorManager.listExecutors().stream().filter(e -> e.isSupport(provider))
                .toList();
        if (candidate.isEmpty()){
            throw new RuntimeException("没有找到合适的执行器");
        }

        if (candidate.size() == 1) {
            return candidate.get(0);
        }

        // 如果有多个执行器,则需要根据各执行器给出的竞价进行评估,选择评分最高的执行器
        return candidate.stream().max((e1, e2) -> e2.bidding(provider) - e1.bidding(provider)).get();
    }

    /**
     * 选择执行器,这里只会简单根据评分选择执行器,如果有多个最佳执行器,则会随机选择一个
     * 后期可以通过重写该方法,实现更复杂的选择逻辑,比如,根据节点负载情况,选择合适的执行器
     * NOTE: 如果需要根据节点负载情况,则有可能需要重写IExecutor接口,添加负载信息
     * @param candidate 执行器列表
     * @param provider 任务提供者
     * @return 执行器
     */
    protected IExecutor selectExecutor(List<IExecutor> candidate, IFunctionRuntimeProvider provider){
        // 如果有多个执行器,则需要根据各执行器给出的竞价进行评估,选择评分最高的执行器
        return candidate.stream().max((e1, e2) -> e2.bidding(provider) - e1.bidding(provider)).get();
    }
}
