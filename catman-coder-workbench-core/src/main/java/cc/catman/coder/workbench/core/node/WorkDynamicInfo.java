package cc.catman.coder.workbench.core.node;

import lombok.Builder;
import lombok.Data;

/**
 * 工作节点动态信息,比如,cpu使用量,内存使用量,磁盘使用量等
 */
@Data
@Builder
public class WorkDynamicInfo {
    /**
     * cpu使用量
     */
    private long cpuUsage;
    /**
     * 内存使用量
     */
    private long memoryUsage;
    /**
     * 磁盘使用量
     */
    private long diskUsage;
    /**
     * gpu使用量
     */
    private long gpuUsage;
}
