package cc.catman.workbench.api.server.websocket;

/**
 * 节点执行器管理器
 */
public class ExecutorManager {
    /**
     * 节点执行器管理器实例
     */
    private static ExecutorManager instance = new ExecutorManager();

    /**
     * 获取节点执行器管理器实例
     * @return 节点执行器管理器实例
     */
    public static ExecutorManager getInstance() {
        return instance;
    }

    /**
     * 私有构造函数
     */
    private ExecutorManager() {
    }

    /**
     * 节点执行器状态变更监听器
     */
    public interface ExecutorStateChangeListener {
        /**
         * 节点执行器状态变更
         * @param executorInformation 节点执行器信息
         * @param oldState 旧状态
         * @param newState 新状态
         */
        void onExecutorStateChange(ExecutorInformation executorInformation, ExecutorState oldState, ExecutorState newState);
    }

    /**
     * 节点执行器状态变更监听器
     */
    private ExecutorStateChangeListener executorStateChangeListener;

    /**
     * 设置节点执行器状态变更监听器
     * @param executorStateChangeListener 节点执行器状态变更监听器
     */
    public void setExecutorStateChangeListener(ExecutorStateChangeListener executorStateChangeListener) {
        this.executorStateChangeListener = executorStateChangeListener;
    }

    /**
     * 节点执行器信息
     */
    private ExecutorInformation executorInformation;

    /**
     * 获取节点执行器信息
     * @return 节点执行器信息
     */
    public ExecutorInformation getExecutorInformation() {
        return executorInformation;
    }

    /**
     * 设置节点执行器信息
     * @param executorInformation 节点执行器信息
     */
    public void setExecutorInformation(ExecutorInformation executorInformation) {
        this.executorInformation = executorInformation;
    }

    /**
     * 节点执行器状态
     */
    private ExecutorState executorState;

    /**
     * 获取节点执行器状态
     * @return 节点执行器状态
     */
    public ExecutorState getExecutorState() {
        return executorState;
    }

    /**
     * 设置节点执行器状态
     * @param executorState 节点执行器状态
     */
    public void setExecutorState(ExecutorState executorState) {
        ExecutorState oldState = this.executorState;
        this.executorState = executorState;
        if (executorStateChangeListener != null) {
            executorStateChangeListener.onExecutorStateChange(executorInformation, oldState, executorState);
        }
    }

}
