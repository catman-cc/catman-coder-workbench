package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.function.FunctionInfo;

import java.util.Optional;

public interface IFunctionInfoLoadStrategy {
    boolean support(String kind);
    
    Optional<FunctionInfo> load(String id, String kind, IFunctionInfoService functionInfoService);
}
