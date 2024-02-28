package cc.catman.workbench.api.server.configuration.join;

/**
 * 执行器实例
 */
public class ExecutorInstance {

    /**
     * 执行器状态
     */
    private ExecutorStatus status;


    public boolean isReady() {
        return status == ExecutorStatus.IDLE || status == ExecutorStatus.RUNNING;
    }
}
