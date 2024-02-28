package cc.catman.coder.workbench.core.node;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 工作节点信息
 */
@Data
@Builder
public class WorkSystemInfo {
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 系统版本
     */
    private String systemVersion;
    /**
     * 系统架构
     */
    private String systemArch;
    /**
     * cpu名称
     */
    private String cpuName;
    /**
     * cpu型号
     */
    private String cpuModel;
    /**
     * cpu物理核数
     */
    private int cpuPhysicalCount;
    /**
     * cpu逻辑核数
     */
    private int cpuLogicalCount;
    /**
     * cpu频率
     */
    private long cpuFrequency;
    /**
     * 内存总量
     */
    private long memoryTotal;
    /**
     * 硬盘总量
     */
    private long diskTotal;

    /**
     * gpu总量
     */
    private long gpuTotal;

    /**
     * gpu名称
     */
    private String gpuName;

    /**
     * mac地址
     */
    private String macAddress;

    /**
     * ip地址
     */
    private String[] ipAddresses;

    private String javaVersion;

    private String javaVendor;

    private String javaWorkSpace;

    /**
     * 额外信息,比如如果是java应用,可以放一些jvm信息之类的内容
     */
    private Map<String,Object> extra;

    /**
     * 动态信息,比如,当前cpu使用量,内存使用量,磁盘使用量等
     */
    private WorkDynamicInfo dynamicInfo;

}
