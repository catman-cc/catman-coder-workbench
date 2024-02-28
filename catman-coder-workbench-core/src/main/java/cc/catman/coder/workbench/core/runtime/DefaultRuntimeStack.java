package cc.catman.coder.workbench.core.runtime;

import lombok.Builder;
import lombok.Getter;
import org.springframework.core.convert.ConversionService;

import java.util.*;

@Builder
public class DefaultRuntimeStack extends AbstractRuntimeStack{

    @Getter
    private String id;
    @Getter
    private String name;

    private IRuntimeStack parentStack;

    @Getter
    private IFunctionVariablesTable variablesTable;
    @Getter
    @Builder.Default
    private int indexOfStack=0;
    @Getter
    @Builder.Default
    private boolean debugMod=false;
    @Getter
    @Builder.Default
    private Map<String, Object> attributes=new HashMap<>();
    @Getter
    @Builder.Default
    private List<IRuntimeStack> childrenStack=new ArrayList<>();
    /**
     * 是否是异步堆栈
     */
    @Builder.Default
    private boolean isAsync=false;

    @Getter
    private IRuntimeStackDistributor distributor;

    @Getter
    private IFunctionRuntimeProvider functionRuntimeProvider;

    @Getter
    private IRuntimeDebuggerContext runtimeDebuggerContext;

    @Getter
    private IRuntimeReportContext runtimeReportContext;

    @Getter
    private IParameterParserManager parameterParserManager;

    private ConversionService conversionService;

    private Object exception;


    private IFunctionVariablesTable createVariablesTable() {
        return new DefaultFunctionVariablesTable();
    }

    @Override
    public boolean isDebugMode() {
        return this.debugMod;
    }

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
    public <T> T convertTo(Object value, Class<T> targetType) {
        return this.conversionService.convert(value, targetType);
    }

    @Override
    public IRuntimeStack createChildStack(String prefix, IFunctionRuntimeProvider provider, IRuntimeStackDistributor distributor) {

        return null;
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
        IRuntimeStackDistributor d = this.getDistributor();
        IFunctionExecutorManager executorManager = d.getExecutorManager();
        IFunctionExecutor executor = executorManager.getExecutor(func);
        return executor.execute(func, this);
    }
}
