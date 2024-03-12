package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.label.ISelector;
import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutorManager;
import cc.catman.coder.workbench.core.schedule.ISchedule;

import java.util.List;
import java.util.UUID;

/**
 * 运行时堆栈分发器,用于创建堆栈,销毁堆栈,调用堆栈
 */
public interface IRuntimeStackDistributor {

   default IRuntimeStack createRuntimeStack(){
         return createRuntimeStack(UUID.randomUUID().toString());
    }

     default IRuntimeStack createRuntimeStack(String name){
          return createRuntimeStack(name,new DefaultFunctionVariablesTable());
   }

    default IRuntimeStack createRuntimeStack(String name,IFunctionVariablesTable variablesTable){
        return createRuntimeStack(name,variablesTable,null);
    }

    IRuntimeStack createRuntimeStack(String name,IFunctionVariablesTable variablesTable,IRuntimeStack parentStack);

    IRuntimeStack createRuntimeStack(IFunctionRuntimeProvider provider);


    IFunctionCallResultInfo call(IFunctionRuntimeProvider provider, IRuntimeStack stack);

    IFunctionCallResultInfo call(IFunctionCallInfo callInfo, IRuntimeStack stack);

   default IFunctionCallResultInfo call(IFunctionRuntimeProvider provider){
       IRuntimeStack stack = this.createRuntimeStack(provider);
         return call(provider,stack);
   }
    /**
     * 获取堆栈信息
     * @param stackId 堆栈id
     * @return 堆栈信息
     */
    IRuntimeStack getRuntimeStack(String stackId);

    /**
     * 获取堆栈信息
     * @param selector 选择器
     * @return 堆栈信息
     */
    List<IRuntimeStack> find(ISelector<?,?> selector);

    void destroy(IRuntimeStack stack);

    IFunctionExecutorManager getExecutorManager();

    ISchedule getSchedule();
}
