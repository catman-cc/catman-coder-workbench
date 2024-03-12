package cc.catman.coder.workbench.core.runtime;


import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;

import java.util.List;
import java.util.Map;

/**
 * 没想好叫啥,就叫这个名字吧.用于提供运行时函数所需的上下文信息
 */
public interface IFunctionRuntimeProvider {
    /**
     * 获取变量表
     * @return 变量表
     */
    IFunctionVariablesTable getVariablesTable();

    /**
     * 获取当前对应的任务信息
     */
    IFunctionCallInfo getFunctionInfo();

    List<Breakpoint> getBreakpoints();

    /**
     * 获取带外数据,用于为方法调用提供一些额外数据,比如,调用链信息,调用者信息等
     * @return 调度器
     */
    Map<String,Object> getOutOfBandData();

    IFunctionRuntimeProvider wrapper(IFunctionCallInfo functionInfo);
}
