package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;

import java.util.List;
import java.util.Optional;

/**
 * 运行时调试器上下文,用于提供调试器所需的上下文信息
 */
public interface IRuntimeDebuggerContext {
    /**
     * 获取当前堆栈的分发器
     *
     * @return 分发器
     */
    IRuntimeStackDistributor getDistributor();

    List<Breakpoint> getBreakpoints();

    /**
     * 获取当前运行时堆栈
     *
     * @return 堆栈信息
     */
    IRuntimeStack getRuntimeStack();

    /**
     * 任务执行开始前调用
     */
    void onStartBreakpoint();

    /**
     * 任务执行结束后尚未返回结果给调用者调用
     */
    void onEndBreakpoint();

    /**
     * 触发断点
     *
     * @param breakpointName 断点名称
     * @param args           断点参数
     */
    default void triggerBreakpoint(String breakpointName, Object... args) {
        IRuntimeStack stack = getRuntimeStack();
        if (!stack.isDebugMode()) {
            return;
        }
        List<Breakpoint> breakpoints = getBreakpoints();
        Optional<Breakpoint> bp = breakpoints.stream().filter(breakpoint -> breakpoint.getInformation().getName().equals(breakpointName))
                .filter(Breakpoint::isEnable)
                .findFirst();
        if (bp.isEmpty()) {
            return;
        }
        boolean isAim = bp
                .map(breakpoint -> {
                    // 有当前堆栈创建子堆栈解析参数
                    IFunctionRuntimeProvider assertProvider = breakpoint.getAssertProvider();
                    IRuntimeStackDistributor distributor = getDistributor();

                    IRuntimeStack childStack = stack.createChildStack("@debug-trigger-breakpoint", assertProvider, distributor);
                    IFunctionCallResultInfo res = distributor.callFunction(assertProvider, childStack);
                    if (res.hasException()) {
                        return false;
                    }
                    return res.getResult().map(o -> (Boolean) o).orElse(false);
                }).orElse(true);
        if (!isAim) {
            return;
        }
        doTriggerBreakpoint(breakpointName, args);
    }

    /**
     * 执行断点触发操作
     *
     * @param breakpointName 断点名称
     * @param args           断点参数
     */
    void doTriggerBreakpoint(String breakpointName, Object... args);
}
