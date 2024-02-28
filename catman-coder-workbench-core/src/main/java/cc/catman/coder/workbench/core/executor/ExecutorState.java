package cc.catman.coder.workbench.core.executor;

/**
 * 执行器状态
 * 1. 准备
 * 2. 运行
 * 3. 停止
 * 4. 禁用
 */
public enum ExecutorState {
    PREPARE,
    RUNNING,
    STOPPED,
    DISABLED,
    ;

    public boolean isRunning() {
        return this == RUNNING;
    }
}
