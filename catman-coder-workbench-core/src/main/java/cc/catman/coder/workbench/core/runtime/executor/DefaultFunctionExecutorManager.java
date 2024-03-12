package cc.catman.coder.workbench.core.runtime.executor;

import cc.catman.coder.workbench.core.runtime.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简单的执行器管理器,直接根据kind获取对应的执行器
 */
public class DefaultFunctionExecutorManager implements IFunctionExecutorManager{

    private Map<String,IFunctionExecutor> executors=new LinkedHashMap<>();

    public DefaultFunctionExecutorManager() {
        this.executors.put("simple",new SimpleFunctionExecutor());
        this.executors.put("if",new IfFunctionExecutor());
        this.executors.put("expression",new ExpressionFunctionExecutor(new SpelExpressionParser()));
    }

    @Override
    public IFunctionExecutor getExecutor(IFunctionRuntimeProvider provider) {
        IFunctionCallInfo callInfo = provider.getFunctionInfo();
        IFunctionInfo funcInfo = callInfo.getFunctionInfo();
        String kind = funcInfo.getKind();
        // 根据kind获取对应的执行器
        IFunctionExecutor executor = executors.get(kind);
        if(executor!=null){
            return executor;
        }
        throw new RuntimeException("没有找到对应的执行器,provider:"+provider);
    }

    @Override
    public IFunctionExecutor getExecutor(IFunctionCallInfo callInfo) {
        return this.executors.get(callInfo.getFunctionInfo().getKind());
    }

    @Override
    public IFunctionExecutorManager register(String kind, IFunctionExecutor executor) {
        executors.put(kind, executor);
        return this;
    }

}
