package cc.catman.coder.workbench.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简单的执行器管理器
 */
public class SimpleFunctionExecutorManager implements IFunctionExecutorManager{

    private Map<String,IFunctionExecutor> executors=new LinkedHashMap<>();

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
        return null;
    }

    @Override
    public IFunctionExecutorManager register(String kind, IFunctionExecutor executor) {
        executors.put(kind, executor);
        return this;
    }

}
