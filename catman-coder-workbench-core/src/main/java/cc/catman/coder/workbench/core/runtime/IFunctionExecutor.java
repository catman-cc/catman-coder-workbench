package cc.catman.coder.workbench.core.runtime;

public interface IFunctionExecutor {
    IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack);

    IFunctionCallResultInfo execute(IFunctionCallInfo callInfo, IRuntimeStack stack);
}
