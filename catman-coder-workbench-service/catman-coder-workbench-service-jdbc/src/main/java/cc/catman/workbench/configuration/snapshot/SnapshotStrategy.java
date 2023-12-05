package cc.catman.workbench.configuration.snapshot;

/**
 * 创建快照的策略,比如,删除数据前
 */
public enum SnapshotStrategy {
    /**
     * 保存快照
     */
    SAVE,
    /**
     * 更新快照
     */
    UPDATE,
    /**
     * 删除快照
     */
    DELETE,
    /**
     * 查询快照
     */
    QUERY
}
