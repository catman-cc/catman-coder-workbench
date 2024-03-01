package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.function.FunctionInfo;

/**
 * 函数信息加载策略
 */
public interface IFunctionInfoManager {
    boolean isInner(String kind);
    boolean support(String kind);

    FunctionInfo load(String id,String kind,IFunctionInfoService functionInfoService);
}
