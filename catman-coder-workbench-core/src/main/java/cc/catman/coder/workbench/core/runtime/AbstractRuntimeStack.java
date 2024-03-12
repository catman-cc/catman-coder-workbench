package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.executor.IExecutor;
import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutorManager;
import cc.catman.coder.workbench.core.schedule.ISchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.core.convert.ConversionService;

import java.util.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public  abstract class AbstractRuntimeStack implements IRuntimeStack{
    @Getter
    protected String id;
    @Getter
    protected String name;

    protected IRuntimeStack parentStack;

    @Getter
    protected IFunctionVariablesTable variablesTable;
    @Getter
    protected int indexOfStack=0;
    @Getter
    protected boolean debugMod=false;
    @Getter
    protected Map<String, Object> attributes=new HashMap<>();
    @Getter
    protected List<IRuntimeStack> childrenStack=new ArrayList<>();
    /**
     * 是否是异步堆栈
     */
    protected boolean isAsync=false;

    /**
     * 是否是远程堆栈
     */
    protected boolean isRemote=false;

    @Getter
    protected IRuntimeStackDistributor distributor;

    @Getter
    protected IFunctionRuntimeProvider functionRuntimeProvider;

    @Getter
    protected IRuntimeDebuggerContext runtimeDebuggerContext;

    @Getter
    protected IRuntimeReportContext runtimeReportContext;

    @Getter
    protected IParameterParserManager parameterParserManager;

    protected ConversionService conversionService;

    protected IFunctionExecutorManager executorManager;

    protected ISchedule schedule;

    protected IRuntimeStackDistributor stackDistributor;

    protected Object exception;


    @Override
    public Optional<IRuntimeStack> getParentStack() {
        return Optional.ofNullable(parentStack);
    }

    @Override
    public Optional<Object> getException() {
        return Optional.ofNullable(exception);
    }

    @Override
    public void reportException(Object exception) {
        this.exception = exception;
        // 设置当前堆栈的异常,并抛出异常信息,该堆栈异常将会被上游堆栈捕获
        if (exception instanceof RuntimeStackException){
            throw (RuntimeStackException) exception;
        }
        throw RuntimeStackException.of(exception,this);
    }

    @Override
    public void reportException(Object exception, String message) {
        this.exception = exception;
        // 设置当前堆栈的异常,并抛出异常信息,该堆栈异常将会被上游堆栈捕获
        if (exception instanceof RuntimeStackException){
            throw (RuntimeStackException) exception;
        }
        throw RuntimeStackException.of(exception,this);
    }

    @Override
    public IRuntimeStack createChildStack(String prefix, IFunctionCallInfo callInfo, Map<String, Object> presetVariables) {
        DefaultFunctionRuntimeProvider frp = DefaultFunctionRuntimeProvider
                .create(callInfo, variablesTable.createChildTable(new HashMap<>()));
        return createChildStack(prefix, frp, this.getDistributor());
    }

    @Override
    public <T> T convertTo(Object value, Class<T> targetType) {
        return this.conversionService.convert(value, targetType);
    }

    @Override
    public void destroy() {
        if (this.isAsync){
            return;
        }
        // 非异步堆栈,需要考虑并等待子堆栈的销毁
        for (IRuntimeStack childStack : childrenStack) {
            childStack.destroy();
        }
    }

    @Override
    public IFunctionCallResultInfo call(IFunctionCallInfo func) {
        // 当前堆栈
        ISchedule schedule=this.getSchedule();
        IFunctionRuntimeProvider provide = this.functionRuntimeProvider.wrapper(func);
        IExecutor exec = schedule.schedule(provide);
        return exec.execute(provide,this);

    }
    @Override
    public boolean isDebugMode() {
        return this.debugMod;
    }

    @Override
    public boolean isAsync() {
        return isAsync;
    }

    @Override
    public boolean isRemote() {
        return isRemote;
    }

    @Override
    public IFunctionExecutorManager getExecutorManager() {
        return this.executorManager;
    }

    @Override
    public ISchedule getSchedule() {
        return schedule;
    }

    protected IFunctionVariablesTable createVariablesTable() {
        return new DefaultFunctionVariablesTable();
    }

    protected String createStackName(String prefix) {
        return prefix+"-"+ UUID.randomUUID().toString();
    }

    protected String createStackId() {
        return UUID.randomUUID().toString();
    }
}
