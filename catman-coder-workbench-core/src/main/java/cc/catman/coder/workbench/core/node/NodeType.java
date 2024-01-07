package cc.catman.coder.workbench.core.node;

public enum NodeType {
    /**
     * 服务节点
     */
    SERVICE,
    /**
     * 服务实例节点
     */
    INSTANCE,
    /**
     * 服务实例节点
     */
    CONFIG,
    /**
     * 服务实例节点
     */
    METADATA,
    /**
     * 服务实例节点
     */
    STATUS,
    /**
     * 服务实例节点
     */
    ENDPOINT,
    /**
     * 服务实例节点
     */
    HEALTH,
    /**
     * 服务实例节点
     */
    DATA,
    /**
     * 服务实例节点
     */
    PROPERTIES,
    /**
     * 服务实例节点
     */
    LEASE,
    /**
     * 服务实例节点
     */
    CLIENTS,
    /**
     * 服务实例节点
     */
    UNKNOWN;

    public static NodeType of(String name) {
        for (NodeType nodeType : NodeType.values()) {
            if (nodeType.name().equalsIgnoreCase(name)) {
                return nodeType;
            }
        }
        return UNKNOWN;
    }
}
