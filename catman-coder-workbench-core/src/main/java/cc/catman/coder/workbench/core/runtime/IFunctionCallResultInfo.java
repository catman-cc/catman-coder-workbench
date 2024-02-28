package cc.catman.coder.workbench.core.runtime;

import java.util.Optional;

/**
 * 函数调用结果信息,需要提供函数的返回值,异常等信息
 */
public interface IFunctionCallResultInfo {

    Optional<Object> getResult();

    Optional<Object> getException();

    /**
     * 获取当前函数调用的总耗时
     */
    Long getTotalTimeSpent();

    Optional<Long> getInternalTimeSpend();

    default boolean hasResult(){
        return getResult().isPresent();
    }

    default boolean hasException(){
        return getException().isPresent();
    }
}
