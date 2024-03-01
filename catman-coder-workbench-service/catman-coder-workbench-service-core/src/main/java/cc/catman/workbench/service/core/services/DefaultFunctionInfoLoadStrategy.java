package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.function.FunctionInfo;

import java.util.Optional;

/**
 * 兜底的函数加载策略
 * 直接就根据id从service中获取函数信息
 */
public class DefaultFunctionInfoLoadStrategy implements IFunctionInfoLoadStrategy{
    @Override
    public boolean support(String kind) {
        return true;
    }

    @Override
    public Optional<FunctionInfo> load(String id, String kind, IFunctionInfoService functionInfoService) {
        if (Optional.ofNullable(id).isEmpty()) {
            return Optional.empty();
        }
        return functionInfoService.findById(id);
    }
}
