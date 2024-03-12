package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionCallResultInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;

/**
 * 本地执行器
 */
public class LocalExecutor extends AbstractExecutor {

    public IFunctionCallResultInfo execute(IFunctionCallInfo callInfo) {
        // 执行任务,本地执行器,需要一个调度器来完成子任务的调度
        return null;
    }

    @Override
    public ExecutorState getState() {
        return null;
    }

    @Override
    public ExecutorState updateState(ExecutorState state) {
        return null;
    }

    @Override
    public int bidding(IFunctionRuntimeProvider provider) {
        return 0;
    }

}
