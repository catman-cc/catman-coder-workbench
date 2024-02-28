package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 节点实例信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExecutorInformation extends Base {

    /**
     * 节点实例id
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;


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
     *  当前执行器支持的函数信息.
     */
    private List<IFunctionInfo> supportedFunctions;

}
