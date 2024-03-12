package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.runtime.IFunctionCallResultInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;
import cc.catman.coder.workbench.core.runtime.IRuntimeStackDistributor;

public interface IExecutor {

    String getId();


    ExecutorState getState();


    ExecutorState updateState(ExecutorState state);


    /**
     * 是否支持该函数
     * @param provider 函数调用信息,用于提供函数所需的上下文信息
     * @return 是否支持
     */
   default boolean isSupport(IFunctionRuntimeProvider provider){
       return this.bidding(provider) >= 0;
   }

    /**
     * 评估函数的执行权重,权重越高,执行的可能性越大,权重越低,执行的可能性越小
     * 如果不支持该函数,则返回-1
     *
     * 非特殊场景下,该方法返回常量值: 100,该值表示当前执行器正常竞争该函数的执行权重
     *
     * 但某些特殊场景下,执行器可能会返回更高的权重值,比如:
     * - 多次调用某一个持久性的资源,比如,mysql,redis等,执行器将返回更高的权重,以确保能够正确的复用相同的资源
     *
     * 该方法是一个双向筛选,当调度器匹配了多个执行器时,将考虑执行器返回的权重
     * 如果调度器只匹配了一个执行器,则会直接调用isSupport方法,不会调用bidding方法
     *
     * @param provider 函数调用信息,用于提供函数所需的上下文信息
     * @return 权重
     */
    int bidding(IFunctionRuntimeProvider provider);

    default void start(){
        this.updateState(ExecutorState.RUNNING);
    }

    default void stop(){
        this.updateState(ExecutorState.STOPPED);
    }

    /**
     * 执行函数
     * @param provider 函数调用信息,用于提供函数所需的上下文信息
     * @param stack 运行时堆栈
     * @return 函数调用结果
     */
    IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack);

    IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStackDistributor stackDistributor);
}
