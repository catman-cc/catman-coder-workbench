package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;

import java.util.Optional;

/**
 * 函数信息服务,用于管理函数信息
 */
public interface IFunctionInfoService {

    /**
     * 根据id查找函数信息
     * @param id 函数id
     * @return 函数信息
     */
    Optional<IFunctionInfo> findById(String id);

    /**
     * 根据id查找函数信息
     * @param id 函数id
     * @param context 上下文,用于解决循环引用
     * @return 函数信息
     */
    Optional<IFunctionInfo> findById(String id, ILoopReferenceContext context);
}
