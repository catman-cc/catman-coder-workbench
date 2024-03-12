package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import cc.catman.workbench.service.core.po.function.EFunctionInfoParamType;
import cc.catman.workbench.service.core.po.function.caller.BreakpointRef;
import cc.catman.workbench.service.core.po.function.caller.FunctionCallInfoArgsAndReturnRef;
import cc.catman.workbench.service.core.po.function.caller.FunctionCallInfoRef;
import cc.catman.workbench.service.core.repossitory.function.caller.IBreakpointRefRepository;
import cc.catman.workbench.service.core.repossitory.function.caller.IFunctionCallInfoArgsAndReturnRefRepository;
import cc.catman.workbench.service.core.repossitory.function.caller.IFunctionCallInfoRefRepository;
import cc.catman.workbench.service.core.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FunctionCallServiceImpl implements IFunctionCallService {

    @Resource
    private ModelMapper modelMapper;
    @Resource
    private IdGenerator idGenerator;
    @Lazy
    @Resource
    private IFunctionInfoService functionInfoService;
    @Lazy
    @Resource
    private IParameterService parameterService;
    @Lazy
    @Resource
    private IBreakpointService breakpointService;
    @Resource
    private IBaseService baseService;

    @Resource
    private IFunctionCallInfoRefRepository functionCallInfoRefRepository;

    @Resource
    private IFunctionCallInfoArgsAndReturnRefRepository functionCallInfoArgsAndReturnRefRepository;

    @Resource
    private IBreakpointRefRepository breakpointRefRepository;

    @Override
    public Optional<FunctionCallInfo> findById(String id, ILoopReferenceContext context) {

        return Optional.empty();
    }

    @Override
    public FunctionCallInfo save(FunctionCallInfo functionCallInfo) {
        // 验证
        validate(functionCallInfo);

        FunctionCallInfoRef fcInfo = modelMapper.map(functionCallInfo, FunctionCallInfoRef.class);

        boolean needUpdate = Optional.ofNullable(fcInfo.getId())
                .map(id -> functionCallInfoRefRepository.existsById(id))
                .orElse(false);

        // 移除旧的参数定义
        List<FunctionCallInfoArgsAndReturnRef> allArgAndReturns = this.functionCallInfoArgsAndReturnRefRepository.findAll(Example.of(FunctionCallInfoArgsAndReturnRef.builder()
                .belongId(fcInfo.getId())
                .build()), Sort.by(Sort.Order.asc("sorting")));
        this.functionCallInfoArgsAndReturnRefRepository.deleteAllInBatch(allArgAndReturns);

        //TODO 尝试移除所有非公开的参数定义,此处现在内存中进行一次校验会更好,因为私有类型的参数,大概率会被原封不动的传递回来
        allArgAndReturns.forEach(item -> {
            this.parameterService.deleteIfNotPublic(item.getParameterId());
        });
        // 依次保存参数定义
        Map<String, Parameter> savedArgs = new LinkedHashMap<>();
        functionCallInfo.getArgs().forEach((k, v) -> {
            Parameter savedParameter = saveParameter(v);
            savedArgs.put(k, savedParameter);
        });


        // 保存函数信息
        FunctionInfo functionInfo = functionCallInfo.getFunctionInfo();
        FunctionInfo savedFunctionInfo = this.saveFunctionInfo(functionInfo);

        // 保存断点信息
        List<Breakpoint> breakpoints = functionCallInfo.getBreakpoints().stream().map(breakpointService::save).toList();

        FunctionCallInfoRef savedFcInfoRef = functionCallInfoRefRepository.saveAndFlush(fcInfo);

        FunctionCallInfo savedFCI = FunctionCallInfo.builder()
                .functionId(savedFunctionInfo.getId())
                .breakpoints(breakpoints)
                .build();
        savedFCI.setArgs(savedArgs);

        // 保存响应信息
        if (functionCallInfo.getResult() != null) {
            Parameter savedResult = saveParameter(functionCallInfo.getResult());
            savedFCI.setResult(savedResult);
        }

        modelMapper.map(savedFcInfoRef, savedFCI);
        return this.baseService.findBaseByBelongId(savedFcInfoRef.getId()).mergeInto(savedFCI);
    }

    @Override
    public FunctionCallInfo create(FunctionInfo functionInfo, ILoopReferenceContext context) {
        FunctionCallInfo functionCallInfo = FunctionCallInfo.builder()
                .id(idGenerator.generateId().toString())
                .functionId(functionInfo.getId())
                .build();
        functionCallInfo.getContext().add(functionCallInfo);

        // 将出入参转换为Parameter
        functionInfo.getArgs().forEach((k, v) -> {
            Parameter parameter = parameterService.createFromTypeDefinition(v).orElseThrow(() -> new RuntimeException("Parameter not found"));
            functionCallInfo.addArg(k, parameter);
        });

        Optional.ofNullable(functionInfo.getResult()).ifPresent(v -> {
            Parameter parameter = parameterService.createFromTypeDefinition(v).orElseThrow(() -> new RuntimeException("Parameter not found"));
            functionCallInfo.setResult(parameter);
        });

        return functionCallInfo;
    }

    public FunctionInfo saveFunctionInfo(FunctionInfo functionInfo) {
        if (Scope.isPublic(functionInfo)) {
            return functionInfo;
        }
        return this.functionInfoService.save(functionInfo);
    }

    public Parameter saveParameter(Parameter parameter) {
        // 忽略公开类型的参数,公开类型的数据,必须显示操作
        if (Scope.isPublic(parameter)) {
            return parameter;
        }
        return this.parameterService.save(parameter);
    }

    protected void validate(FunctionCallInfo functionCallInfo) {

    }

    public Optional<FunctionCallInfo> doFindById(String id, ILoopReferenceContext context) {
        FunctionCallInfoRef functionCallInfoRef = this.functionCallInfoRefRepository.
                findById(id).orElseThrow(() -> new RuntimeException("FunctionCallInfo not found"));
        FunctionCallInfo functionCallInfo = modelMapper.map(functionCallInfoRef, FunctionCallInfo.class);
        context.add(functionCallInfo);
        Map<String, Parameter> args = new HashMap<>();
        functionCallInfo.setArgs(args);


        // 获取出入参数定义
        Map<EFunctionInfoParamType, ? extends List<FunctionCallInfoArgsAndReturnRef>> parameters = this.functionCallInfoArgsAndReturnRefRepository.findAll(Example.of(FunctionCallInfoArgsAndReturnRef.builder()
                .belongId(id)
                .build()), Sort.by(Sort.Order.asc("sorting"))).stream().collect(Collectors.groupingBy(FunctionCallInfoArgsAndReturnRef::getType));

        // 解析参数定义
        parameters.get(EFunctionInfoParamType.INPUT).forEach(item -> {
            // 获取参数定义
            Parameter parameter = parameterService.findById(item.getParameterId(), context).orElseThrow(() -> new RuntimeException("Parameter not found"));
            args.put(item.getName(), parameter);
        });

        functionCallInfo.setResult(
                parameters.containsKey(EFunctionInfoParamType.OUTPUT) ? parameters.get(EFunctionInfoParamType.OUTPUT).stream().findFirst().map(item -> {
                    // 获取参数定义
                    return parameterService.findById(item.getParameterId(), context).orElseThrow(() -> new RuntimeException("Parameter not found"));
                }).get() : null
        );


        functionCallInfo.setFunctionId(functionCallInfoRef.getFunctionId());

        // 获取对应的函数定义,这里忽略返回值,但是确保上下文中有函数定义
        IFunctionInfo functionInfo = this.functionInfoService
                .findById(functionCallInfoRef.getFunctionId(), context).orElseThrow(() -> new RuntimeException("FunctionInfo not found"));


        // 获取断点定义
        List<BreakpointRef> breakpointRefs = breakpointRefRepository.findAll(Example.of(BreakpointRef.builder()
                .functionCallerId(id)
                .build()), Sort.by(Sort.Order.asc("sorting")));

        // 构建响应对象
        functionCallInfo.setBreakpoints(breakpointRefs.stream().map(br -> modelMapper.map(br, Breakpoint.class)).collect(Collectors.toList()));

        return Optional.of(functionCallInfo);
    }
}
