package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.executor.IExecutor;
import cc.catman.coder.workbench.core.label.ISelector;
import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutorManager;
import cc.catman.coder.workbench.core.schedule.ISchedule;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.convert.ConversionService;

import java.util.*;

@Builder
public class DefaultRuntimeStackDistributor implements IRuntimeStackDistributor{

    @Builder.Default
    private Map<String,IRuntimeStack> stacks=new HashMap<>();

    private IRuntimeReportContext reportContext;

    private IRuntimeDebuggerContext debuggerContext;

    private IParameterParserManager parameterParserManager;

    @Getter
    private IFunctionExecutorManager executorManager;

    private ConversionService conversionService;

    @Getter
    private ISchedule schedule;

    @Override
    public IRuntimeStack createRuntimeStack(String name, IFunctionVariablesTable variablesTable, IRuntimeStack parentStack) {
        String stackName= Optional.ofNullable(name).orElse(createStackName("stack"));

        IRuntimeStack newStack = DefaultRuntimeStack.builder()
                .id(createStackId())
                .name(stackName)
                .parentStack(parentStack)
                .variablesTable(variablesTable)
                .distributor(this)
                .functionRuntimeProvider(DefaultFunctionRuntimeProvider.create(null,variablesTable))
                .runtimeDebuggerContext(debuggerContext)
                .runtimeReportContext(reportContext)
                .parameterParserManager(parameterParserManager)
                .conversionService(conversionService)
                .schedule(schedule)
                .executorManager(executorManager)
                .isAsync(false)
                .indexOfStack(0)
                .debugMod(false)
                .build();
        newStack=postCreateRuntimeStack(newStack);
        this.stacks.put(newStack.getId(),newStack);
        return newStack;
    }

    protected IRuntimeStack postCreateRuntimeStack(IRuntimeStack stack){
        return stack;
    }

    protected String createStackId(){
        return UUID.randomUUID().toString();
    }

    protected String createStackName(String prefix){
        return prefix + UUID.randomUUID();
    }

    @Override
    public IRuntimeStack createRuntimeStack(IFunctionRuntimeProvider provider) {
        IFunctionCallInfo callInfo = provider.getFunctionInfo();
        IFunctionInfo functionInfo = callInfo.getFunctionInfo();
        String stackName= Optional.ofNullable(functionInfo.getName()).orElse(createStackName("stack"));

        IRuntimeStack newStack = DefaultRuntimeStack.builder()
                .id(createStackId())
                .name(stackName)
                .parentStack(null)
                .variablesTable(provider.getVariablesTable())
                .distributor(this)
                .functionRuntimeProvider(provider)
                .runtimeDebuggerContext(debuggerContext)
                .runtimeReportContext(reportContext)
                .parameterParserManager(parameterParserManager)
                .conversionService(conversionService)
                .schedule(schedule)
                .executorManager(executorManager)
                .isAsync(false)
                .indexOfStack(0)
                .debugMod(false)
                .build();
        newStack=postCreateRuntimeStack(newStack);
        this.stacks.put(newStack.getId(),newStack);
        return newStack;
    }

    @Override
    public IFunctionCallResultInfo call(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        ISchedule sc=this.getSchedule();
        // 通过调度器获取一个执行器,该执行器可能是一个本地执行器,也可能是一个远程执行器
        IExecutor exec = sc.schedule(provider);
        // 执行器执行任务并获取返回结果,值得注意的是,远程任务的堆栈信息也是在远程创建的,在debug模式下,需要考虑如何将远程堆栈信息传递到本地
        // 在非debug模式下,远程堆栈信息是不需要传递到本地的,因为远程堆栈信息是不需要本地进行操作的
        // 为了能够访问到远程堆栈信息,远程执行器需要提供一个远程堆栈信息的访问接口,该接口可以通过远程调用的方式获取远程堆栈信息
        // 同时远程执行器还需要考虑支持debug模式,所以远程执行器,远程执行器在执行一个任务时,除了任务信息和参数信息外,还需要携带额外的数据,比如可能涉及到的子堆栈的debug信息
        // 断点信息,其实是和任务定义id一一对应的,
        return exec.execute(provider,stack);
    }

    @Override
    public IFunctionCallResultInfo call(IFunctionCallInfo callInfo, IRuntimeStack stack) {
        return stack.call(callInfo);
    }

    @Override
    public IFunctionCallResultInfo call(IFunctionRuntimeProvider provider) {
        IRuntimeStack stack = this.createRuntimeStack(provider);
        ISchedule sc=this.getSchedule();
        // 通过调度器获取一个执行器,该执行器可能是一个本地执行器,也可能是一个远程执行器
        IExecutor exec = sc.schedule(provider);
        // 执行器执行任务并获取返回结果,值得注意的是,远程任务的堆栈信息也是在远程创建的,在debug模式下,需要考虑如何将远程堆栈信息传递到本地
        // 在非debug模式下,远程堆栈信息是不需要传递到本地的,因为远程堆栈信息是不需要本地进行操作的
        // 为了能够访问到远程堆栈信息,远程执行器需要提供一个远程堆栈信息的访问接口,该接口可以通过远程调用的方式获取远程堆栈信息
        // 同时远程执行器还需要考虑支持debug模式,所以远程执行器,远程执行器在执行一个任务时,除了任务信息和参数信息外,还需要携带额外的数据,比如可能涉及到的子堆栈的debug信息
        // 断点信息,其实是和任务定义id一一对应的,
        return exec.execute(provider,stack);
    }

    @Override
    public IRuntimeStack getRuntimeStack(String stackId) {
        return this.stacks.get(stackId);
    }

    @Override
    public List<IRuntimeStack> find(ISelector<?, ?> selector) {
        return null;
    }

    @Override
    public void destroy(IRuntimeStack stack) {
        // 主要是释放堆栈资源,一个堆栈在销毁时,需要判断子堆栈是否已经销毁,如果没有销毁,则需要先销毁子堆栈
        // 但是考虑到异步堆栈的存在,所以需要考虑异步堆栈的销毁问题,理论上有可能整个程序堆栈都退出了,但是异步堆栈还在执行
        // 因此,主堆栈不考虑等待子堆栈中的异步堆栈,但是异步堆栈持有父堆栈的引用,因此当父堆栈销毁时,需要通知子堆栈,由子堆栈考虑如何进行销毁处理.
        this.stacks.remove(stack.getId());
    }

}
