package cc.catman.coder.workbench.core.runtime.executor;

import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;

/**
 * 函数执行器管理器
 * 用于管理函数执行器,通常情况下会根据函数的类型来选择执行器
 * 但是,该执行器可以实现更为复杂的逻辑
 */
public interface IFunctionExecutorManager {

    IFunctionExecutor getExecutor(IFunctionRuntimeProvider provider);

    IFunctionExecutor getExecutor(IFunctionCallInfo callInfo);

   IFunctionExecutorManager register(String kind,IFunctionExecutor executor);
}
