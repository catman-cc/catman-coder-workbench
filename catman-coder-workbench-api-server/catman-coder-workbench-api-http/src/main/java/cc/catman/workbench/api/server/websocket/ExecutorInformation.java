package cc.catman.workbench.api.server.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点实例信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutorInformation {

    /**
     * 通用的系统名称
     */
    private String systemName;
    /**
     * 系统名称
     */
     private String systemManufacturer;

    /**
     * 系统位数,32或者64,如果为0,则表示未知
     */
    private int systemBitness;
    /**
     * 系统版本
     */
    private String systemVersion;

    /**
     * cpu核心数
     */
    private int cpuCount;

    /**
     * 内存大小,单位为MB
     */
    private int memorySize;

    /**
     * 操作系统名称
     */
    private String osName;
    /**
     * 操作系统版本
     */
    private String osVersion;
    /**
     * 操作系统架构
     */
    private String osArch;

    /**
     * 操作系统位数,32或者64,如果为0,则表示未知
     */
    private int osBitness;

    /**
     * 运行时名称,比如java,python等
     */
    private String runtimeName;

    /**
     * 运行时版本,比如java版本:1.8.0_181,python版本:3.6.5,go版本:1.10.3
     */
    private String runtimeVersion;

    /**
     * 运行时位数,32或者64,如果为0,则表示未知
     */
    private int runtimeBitness;

    /**
     * 用户名
     */
    private String userName;
}
