package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutor;

public class SimpleFunctionExecutor implements IFunctionExecutor {
    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        IFunctionCallInfo callInfo = provider.getFunctionInfo();
        String value = callInfo.getConfig();
        return DefaultFunctionCallResultInfo.of(value);
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionCallInfo callInfo, IRuntimeStack stack) {
        return DefaultFunctionCallResultInfo.of(callInfo.getConfig());
    }

}
