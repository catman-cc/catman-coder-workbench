package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
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
import cc.catman.workbench.service.core.services.IFunctionCallService;
import cc.catman.workbench.service.core.services.IFunctionInfoService;
import cc.catman.workbench.service.core.services.IParameterService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FunctionCallServiceImpl implements IFunctionCallService {

    @Resource
    private ModelMapper modelMapper;
    @Lazy
    @Resource
    private IFunctionInfoService functionInfoService;
    @Lazy
    @Resource
    private IParameterService parameterService;

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

    public Optional<FunctionCallInfo> doFindById(String id, ILoopReferenceContext context) {
        FunctionCallInfoRef functionCallInfoRef = this.functionCallInfoRefRepository.
                findById(id).orElseThrow(() -> new RuntimeException("FunctionCallInfo not found"));
        FunctionCallInfo functionCallInfo=modelMapper.map(functionCallInfoRef,FunctionCallInfo.class);
        context.add(functionCallInfo);
        Map<String,Parameter> args=new HashMap<>();
        functionCallInfo.setArgs(args);


        // 获取出入参数定义
        Map<EFunctionInfoParamType, ? extends List<FunctionCallInfoArgsAndReturnRef>> parameters = this.functionCallInfoArgsAndReturnRefRepository.findAll(Example.of(FunctionCallInfoArgsAndReturnRef.builder()
                .belongId(id)
                .build()), Sort.by(Sort.Order.asc("sorting"))).stream().collect(Collectors.groupingBy(FunctionCallInfoArgsAndReturnRef::getType));

        // 解析参数定义

        parameters.get(EFunctionInfoParamType.INPUT).forEach(item -> {
            // 获取参数定义
            Parameter parameter= parameterService.findById(item.getParameterId(), context).orElseThrow(() -> new RuntimeException("Parameter not found"));
            args.put(item.getName(),parameter);
        });

        functionCallInfo.setResult(
                parameters.containsKey(EFunctionInfoParamType.OUTPUT)?parameters.get(EFunctionInfoParamType.OUTPUT).stream().findFirst().map(item -> {
                    // 获取参数定义
                    return parameterService.findById(item.getParameterId(), context).orElseThrow(() -> new RuntimeException("Parameter not found"));
                }).get():null
        );


        functionCallInfo.setFunctionId(functionCallInfoRef.getFunctionId());

        // 获取对应的函数定义,这里忽略返回值,但是确保上下文中有函数定义
        IFunctionInfo functionInfo=this.functionInfoService
                .findById(functionCallInfoRef.getFunctionId(), context).orElseThrow(() -> new RuntimeException("FunctionInfo not found"));


        // 获取断点定义
        List<BreakpointRef> breakpointRefs= breakpointRefRepository.findAll(Example.of(BreakpointRef.builder()
                        .functionCallerId(id)
                .build()),Sort.by(Sort.Order.asc("sorting")));

        // 构建响应对象
        functionCallInfo.setBreakpoints(breakpointRefs.stream().map(br-> modelMapper.map(br, Breakpoint.class)).collect(Collectors.toList()));

        return Optional.of(functionCallInfo);
    }
}
