package cc.catman.coder.workbench.core.runtime;

import lombok.Data;

/**
 * 函数调用异常结果
 */
@Data
public class IFunctionCallExceptionResult {
    /**
     * 异常是否被处理
     */
    private boolean isHandled;

    /**
     * 异常处理结果
     */
    private IFunctionCallResultInfo resultInfo;

    public IFunctionCallExceptionResult(boolean isHandled, IFunctionCallResultInfo resultInfo) {
        this.isHandled = isHandled;
        this.resultInfo = resultInfo;
    }

    public static IFunctionCallExceptionResult unhandled() {
        return new IFunctionCallExceptionResult(false, null);
    }

    public static IFunctionCallExceptionResult handled(IFunctionCallResultInfo resultInfo) {
        return new IFunctionCallExceptionResult(true, resultInfo);
    }

    public boolean isHandled() {
        return isHandled;
    }

}
