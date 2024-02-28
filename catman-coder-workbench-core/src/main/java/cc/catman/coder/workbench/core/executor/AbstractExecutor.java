package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.runtime.*;
import lombok.Getter;

import java.util.List;

public abstract class AbstractExecutor implements IExecutor{
    @Getter
    protected String id;
    @Getter
    protected ExecutorInformation information;

    protected IFunctionExecutorManager functionExecutorManager;

    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        IFunctionVariablesTable vars = provider.getVariablesTable();
        IFunctionCallInfo info = provider.getFunctionInfo();
        // 寻找任务对应的执行器
        IFunctionExecutor functionExecutor= this.functionExecutorManager.getExecutor(provider);

        // Note: 任务执行器需要完成自身参数的解析工作,并合理的配置子堆栈
        IFunctionCallResultInfo result = functionExecutor.execute(provider, stack);
        // 此时任务执行完成,需要将结果写入到任务的上下文中,其中包括将返回值写入到临时变量表中
        if (result.hasException()){
            // 任务执行时,发生异常,获取当前堆栈的异常处理器,如果有任意一个异常处理器能给处理该异常,则异常不会传递到上层堆栈
            List<IFunctionCallExceptionHandler> exceptionHandlers = info.getExceptionHandlers();
            for (IFunctionCallExceptionHandler eh : exceptionHandlers) {
                IFunctionCallExceptionResult er = eh.handleException(provider, stack, result);
                if (er.isHandled()){
                  return this.processFunctionCallResult(provider, stack, result);
                }
            }
            // 异常没有被处理,则将异常传递到上层堆栈
            stack.reportException(result.getException());
            return result;
        }
        vars.setVariable(info.resultName(), result.getResult());
        return result;
    }

    protected IFunctionCallResultInfo processFunctionCallResult(IFunctionRuntimeProvider provider, IRuntimeStack stack, IFunctionCallResultInfo result){
        IFunctionVariablesTable vars = provider.getVariablesTable();
        IFunctionCallInfo info = provider.getFunctionInfo();
        if (result.hasException()){
            List<IFunctionCallExceptionHandler> exceptionHandlers = info.getExceptionHandlers();
            for (IFunctionCallExceptionHandler eh : exceptionHandlers) {
                IFunctionCallExceptionResult er = eh.handleException(provider, stack, result);
                if (er.isHandled()){
                    return er.getResultInfo();
                }
            }
        }
        vars.setVariable(info.resultName(), result.getResult());
        return null;
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStackDistributor stackDistributor) {
        // 执行任务之前,先创建该任务的运行时堆栈
        IRuntimeStack stack = stackDistributor.createRuntimeStack(provider);
        // 执行任务
        return execute(provider, stack);
    }
}
