package cc.catman.coder.workbench.core.runtime.executor;

import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionCallResultInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;

public interface IFunctionExecutor {
    IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack);

    IFunctionCallResultInfo execute(IFunctionCallInfo callInfo, IRuntimeStack stack);
}
