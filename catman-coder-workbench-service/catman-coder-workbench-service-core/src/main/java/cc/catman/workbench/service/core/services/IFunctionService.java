package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.function.FunctionInfo;

import java.util.Optional;

/**
 * 函数服务
 */
public interface IFunctionService {

    /**
     * 判断一个函数是否是内置函数
     * @param id 函数id
     * @return 是否是内置函数
     */
    boolean isInnerFunction(String id);

    Optional<FunctionInfo> findById(String id);

    Optional<FunctionInfo> findById(String id, ILoopReferenceContext context);
}
