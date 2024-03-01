package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.function.FunctionInfo;

import java.util.*;

public class DefaultFunctionInfoManager implements IFunctionInfoManager {

    /**
     * 内置函数信息,这些函数信息是不需要从service中获取的
     * 比如,if,for等
     */
    private Map<String, FunctionInfo> innerFunctionInfos = new HashMap<>();

    private List<IFunctionInfoLoadStrategy> functionInfoLoadStrategies = new ArrayList<>();

    private IFunctionInfoLoadStrategy defaultFunctionInfoLoadStrategy = new DefaultFunctionInfoLoadStrategy();

    @Override
    public boolean isInner(String kind) {
        return innerFunctionInfos.containsKey(kind);
    }

    @Override
    public boolean support(String kind) {
        return isInner(kind) || functionInfoLoadStrategies.stream().anyMatch(strategy -> strategy.support(kind));
    }

    @Override
    public FunctionInfo load(String id, String kind, IFunctionInfoService functionInfoService) {
        // 根据kind进行初步筛选,获取对应的函数信息
        if (innerFunctionInfos.containsKey(kind)) {
            return innerFunctionInfos.get(kind);
        }

        return this.functionInfoLoadStrategies.stream()
                .filter(strategy -> strategy.support(kind))
                .map(strategy -> strategy.load(id, kind, functionInfoService))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseGet(() -> Optional.ofNullable(defaultFunctionInfoLoadStrategy)
                        .map(strategy -> strategy.load(id, kind, functionInfoService))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .orElse(null));
    }
}
