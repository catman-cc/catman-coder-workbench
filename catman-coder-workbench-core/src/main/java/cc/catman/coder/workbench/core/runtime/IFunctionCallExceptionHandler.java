package cc.catman.coder.workbench.core.runtime;

/**
 * 函数调用异常处理器
 */
public interface IFunctionCallExceptionHandler {

    IFunctionCallExceptionResult handleException(IFunctionRuntimeProvider provider, IRuntimeStack stack, IFunctionCallResultInfo resultInfo);
}
