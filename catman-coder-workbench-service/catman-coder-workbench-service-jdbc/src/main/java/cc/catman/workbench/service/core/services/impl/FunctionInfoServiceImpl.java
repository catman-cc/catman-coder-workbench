package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.workbench.service.core.po.function.*;
import cc.catman.workbench.service.core.repossitory.function.IFunctionCallInfoQueueRefRepository;
import cc.catman.workbench.service.core.repossitory.function.IFunctionInfoArgsAndReturnRefRepository;
import cc.catman.workbench.service.core.repossitory.function.IFunctionInfoRefRepository;
import cc.catman.workbench.service.core.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FunctionInfoServiceImpl implements IFunctionInfoService {

    @Resource
    private ModelMapper modelMapper;
    @Lazy
    @Resource
    private IFunctionCallService functionCallService;
    @Lazy
    @Resource
    private IParameterService parameterService;
    @Lazy
    @Resource
    private ITypeDefinitionService typeDefinitionService;

    @Lazy
    @Resource
    private IBreakpointService breakpointService;
    @Resource
    private IBaseService baseService;

    @Resource
    IFunctionInfoManager functionInfoManager;

    @Resource
    private IFunctionInfoRefRepository functionInfoRefRepository;

    @Resource
    private IFunctionInfoArgsAndReturnRefRepository  functionInfoArgsAndReturnRefRepository;

    @Resource
    private IFunctionCallInfoQueueRefRepository functionCallInfoQueueRefRepository;


    @Override
    public Optional<FunctionInfo> findById(String id) {
        return this.findById(id,ILoopReferenceContext.create());
    }

    @Override
    public Optional<FunctionInfo> findById(String id, ILoopReferenceContext context) {
        // 获取函数定义
        Optional<FunctionInfo> fi = context.getFunctionInfo(id, (c) -> this.doFindById(id, context));

        // 获取函数的入参和出参定义
        // 获取函数的调用队列
        // 获取函数的其余信息

        return Optional.empty();
    }

    @Override
    public FunctionInfo save(FunctionInfo functionInfo) {
        if(nonPersistentFunction(functionInfo)){
            return functionInfo;
        }

        validate(functionInfo);

        FunctionInfoRef fir = this.functionInfoRefRepository.save(this.modelMapper.map(functionInfo, FunctionInfoRef.class));

        // 移除旧的参数定义
        this.deleteTypeDefinitionIfNotPublic(functionInfo);
        // 保存参数类型定义
        AtomicInteger argSorting = new AtomicInteger(0);
        functionInfo.getArgs().forEach((k,v)->{
            // 保存类型定义
            TypeDefinition td = Scope.isPublic(v)?v: this.typeDefinitionService.save(v);
            FunctionInfoArgsAndReturnRef fiar = FunctionInfoArgsAndReturnRef.builder()
                    .belongId(fir.getId())
                    .type(EFunctionInfoParamType.INPUT)
                    .typeDefinitionId(td.getId())
                    .name(k)
                    .sorting(argSorting.getAndIncrement())
                    .build();
            this.functionInfoArgsAndReturnRefRepository.save(fiar);
        });

        // 保存返回值类型定义
        if (functionInfo.getResult()!=null){
            TypeDefinition td = Scope.isPublic(functionInfo.getResult())?functionInfo.getResult(): this.typeDefinitionService.save(functionInfo.getResult());
            FunctionInfoArgsAndReturnRef fiar = FunctionInfoArgsAndReturnRef.builder()
                    .belongId(fir.getId())
                    .type(EFunctionInfoParamType.OUTPUT)
                    .typeDefinitionId(td.getId())
                    .sorting(argSorting.getAndIncrement())
                    .build();
            this.functionInfoArgsAndReturnRefRepository.save(fiar);
        }


        AtomicInteger callQueueSorting = new AtomicInteger(0);
        // 保存调用队列
        functionInfo.getCallQueue().forEach(item->{

            FunctionCallInfo fci =Scope.isPublic(item)
                    ?this.functionCallService.findById(item.getId()).orElseThrow()
                    : this.functionCallService.save(item);

            // 保存调用队列引用
            functionCallInfoQueueRefRepository.save(FunctionCallInfoQueueRef.builder()
                    .belongId(fir.getId())
                            .type(EFunctionCallType.QUEUE)
                    .functionCallInfoId(fci.getId())
                            .sorting(callQueueSorting.getAndIncrement())
                    .build());
        });

        AtomicInteger finallySorting = new AtomicInteger(0);
        // 保存finally调用队列
        functionInfo.getFinallyCalls().forEach(item->{
            FunctionCallInfo fci =Scope.isPublic(item)
                    ?this.functionCallService.findById(item.getId()).orElseThrow()
                    : this.functionCallService.save(item);
            // 保存调用队列引用
            functionCallInfoQueueRefRepository.save(FunctionCallInfoQueueRef.builder()
                    .belongId(fir.getId())
                    .type(EFunctionCallType.FINAL)
                    .functionCallInfoId(fci.getId())
                    .sorting(finallySorting.getAndIncrement())
                    .build());
        });

        // 保存基础断点信息,目前没有想到能够动态添加断点信息的场景
        // 唯一有可能的就是自定义函数,然后有选择的放开函数内部的几个断点信息
        functionInfo.getBreakpointInformation().forEach(item->{

        });

        return this.findById(fir.getId()).orElseThrow(()->new RuntimeException("FunctionInfo not found by id: " + fir.getId()));
    }

    @Override
    public FunctionInfo findByKind(String kind) {
        return this.functionInfoManager.load(null,kind,this);
    }

    @Override
    public FunctionInfo fillIfNeed(FunctionInfo functionInfo) {
        // 此处填充函数会忽略内部数据,仅在意id,kind,出入参类型
        if (functionInfo == null){
            return null;
        }
        FunctionInfo load = this.functionInfoManager.load(functionInfo.getId(), functionInfo.getKind(), this);
        if(Optional.ofNullable(load).isPresent()){
            return load;
        }
        // 如果没有加载到函数信息,该如何处理?
        return functionInfo;
    }

    @Override
    public boolean isInnerFunction(String kind) {
       return this.functionInfoManager.isInner(kind);
    }

    protected void deleteTypeDefinitionIfNotPublic(FunctionInfo functionInfo){
        List<FunctionInfoArgsAndReturnRef> all = this.functionInfoArgsAndReturnRefRepository.findAll(Example.of(FunctionInfoArgsAndReturnRef.builder()
                .belongId(functionInfo.getId())
                .build()));
        // 删除类型引用
        this.functionInfoArgsAndReturnRefRepository.deleteAll(all);
        // 然后删除引用的所有非公开的类型
        all.forEach(item->{
            this.typeDefinitionService.deleteIfNotPublic(item.getTypeDefinitionId());
        });
    }
    /**
     * 验证函数是否是非持久化类型的,比如,kind是if,while,for等
     * @param functionInfo 函数定义
     * @return 是否是非持久化类型的
     */
    protected boolean nonPersistentFunction(IFunctionInfo functionInfo){
      return false;
    }
    protected void validate(IFunctionInfo functionInfo){
        // 验证函数定义
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
