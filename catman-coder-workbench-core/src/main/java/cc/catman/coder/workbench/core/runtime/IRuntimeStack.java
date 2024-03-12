package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutorManager;
import cc.catman.coder.workbench.core.schedule.ISchedule;

import java.util.*;

/**
 * 运行时堆栈信息
 * 理论上堆栈信息是从上而下,一对一的,因为每一个堆栈在运行结束之后都会被销毁
 * 但是,因为考虑到异步任务的情况,所以,堆栈信息可能会出现多对一的情况
 */
public interface IRuntimeStack {

    /**
     * 获取堆栈的id
     */
    String getId();

    /**
     * 获取堆栈的名称
     */
    String getName();

    /**
     * 获取堆栈的属性,该属性数据可以由任务填充
     */
    Map<String,Object> getAttributes();

    /**
     * 获取当前堆栈的变量表
     * @return 变量表
     */
    IFunctionVariablesTable getVariablesTable();

    IFunctionExecutorManager getExecutorManager();

    ISchedule getSchedule();

    default List<String> getFullName(){
        if (getParentStack().isPresent()) {
            List<String> stackIds = getParentStack().get().getFullName();
            stackIds.add(getName());
            return stackIds;
        } else {
            List<String> names=new ArrayList<>();
            names.add(this.getName());
            return  names;
        }
    }

    /**
     * 获取堆栈的所有id,包括父堆栈的id
     * @return 堆栈id
     */
    default List<String> getStackIds(){
        if (getParentStack().isPresent()) {
            List<String> stackIds = getParentStack().get().getStackIds();
            stackIds.add(getId());
            return stackIds;
        } else {
            List<String> ids=new ArrayList<>();
            ids.add(this.getId());
            return  ids;
        }
    }

    /**
     * 获取堆栈的索引,索引从0开始
     *
     * @return 堆栈索引
     */
    int getIndexOfStack();

    /**
     * 获取堆栈的调试模式
     * @return 是否是调试模式
     */
    boolean isDebugMode();

    /**
     * 获取堆栈的父堆栈
     *
     * @return 父堆栈
     */
    Optional<IRuntimeStack> getParentStack();

    /**
     * 获取堆栈的子堆栈
     *
     * @return 子堆栈
     */
    List<IRuntimeStack> getChildrenStack();

    /**
     * 获取当前堆栈的分发器
     *
     * @return 分发器
     */
    IRuntimeStackDistributor getDistributor();

    /**
     * 获取当前堆栈的调试器上下文
     * @return 调试器上下文
     */
    IRuntimeDebuggerContext getRuntimeDebuggerContext();

    /**
     * 获取当前堆栈的报告上下文
     * @return 报告上下文
     */
    IRuntimeReportContext getRuntimeReportContext();

    IParameterParserManager getParameterParserManager();

    /**
     * 获取当前堆栈的异常
     * @return 异常
     */
    Optional<Object> getException();

    void reportException(Object exception);

    void reportException(Object exception, String message);

    <T> T convertTo(Object value, Class<T> targetType);

    IRuntimeStack createChildStack(String prefix, IFunctionRuntimeProvider provider, IRuntimeStackDistributor distributor);

    IRuntimeStack createChildStack(String prefix,IFunctionCallInfo callInfo, Map<String,Object> presetVariables);

    default IRuntimeStack createChildStack(String prefix,IFunctionCallInfo callInfo){
        return createChildStack(prefix,callInfo,new HashMap<>());
    }

    void destroy();

    /**
     * 在当前堆栈中调用一个函数,并返回函数调用的结果,需要注意的是,调用函数时,会在当前堆栈中创建子堆栈来运行函数
     * @param func 函数调用信息
     * @return 函数调用结果
     */
    IFunctionCallResultInfo call(IFunctionCallInfo func);

    boolean isAsync();

    boolean isRemote();
}
