package cc.catman.coder.workbench.core.executor;

/**
 * 接入码重复使用策略
 */
public enum EJoinCodeRepeatStrategy {
    /**
     * 当接入码被使用时,将拒绝其他使用该接入码的请求
     */
    DENY,

    /**
     * 当接入码被使用时,将停止之前使用该接入码接入的节点,并接入新的节点
     */
    REPLACE,

    /**
     * 当接入码被使用时,将拒绝其他使用该接入码的请求,并将接入码设置为无效
     */
    DENY_AND_INVALID,

    /**
     * 当接入码被使用时,将停止之前使用该接入码接入的节点,并接入新的节点,并将接入码设置为无效
     */
    REPLACE_AND_INVALID,
}
