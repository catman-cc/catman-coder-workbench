package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.workbench.service.core.po.function.EFunctionInfoParamType;
import cc.catman.workbench.service.core.po.function.FunctionInfoArgsAndReturnRef;
import cc.catman.workbench.service.core.po.function.FunctionInfoRef;
import cc.catman.workbench.service.core.repossitory.function.IFunctionInfoArgsAndReturnRefRepository;
import cc.catman.workbench.service.core.repossitory.function.IFunctionInfoRefRepository;
import cc.catman.workbench.service.core.services.IFunctionInfoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class FunctionInfoServiceImpl implements IFunctionInfoService {

    @Resource
    private IFunctionInfoRefRepository functionInfoRefRepository;

    @Resource
    private IFunctionInfoArgsAndReturnRefRepository  functionInfoArgsAndReturnRefRepository;


    @Override
    public Optional<IFunctionInfo> findById(String id) {
        return this.findById(id,ILoopReferenceContext.create());
    }

    @Override
    public Optional<IFunctionInfo> findById(String id, ILoopReferenceContext context) {
        // 获取函数定义
        Optional<FunctionInfo> fi = context.getFunctionInfo(id, (c) -> this.doFindById(id, context));

        // 获取函数的入参和出参定义
        // 获取函数的调用队列
        // 获取函数的其余信息

        return Optional.empty();
    }

    public Optional<FunctionInfo> doFindById(String id, ILoopReferenceContext context) {
        Optional<FunctionInfoRef> firOpt = this.functionInfoRefRepository.findById(id);
        if (firOpt.isEmpty()){
            throw new RuntimeException("FunctionInfoRef not found by id: " + id);
        }
        FunctionInfoRef fir = firOpt.get();
        // 获取参数定义
       Map<EFunctionInfoParamType,List<FunctionInfoArgsAndReturnRef>> parameters  = this.functionInfoArgsAndReturnRefRepository
                .findAll(Example.of(FunctionInfoArgsAndReturnRef
                        .builder()
                        .belongId(id)
                        .build()), Sort.by(Sort.Order.asc("sorting")))
                .stream()
                .collect(Collectors.groupingBy(FunctionInfoArgsAndReturnRef::getType));


        // 构建请求参数

        // 构建返回参数
        return null;
    }
}
