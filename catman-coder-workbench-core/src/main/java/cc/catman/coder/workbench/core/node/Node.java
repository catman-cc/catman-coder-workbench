package cc.catman.coder.workbench.core.node;

import cc.catman.coder.workbench.core.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 *  注册到集群中提供工作能力的节点,比如:
 *  - api-server: 提供核心的api服务
 *
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Node extends Base {

    private String id;

    private String name;

    private List<String> ips;

    /**
     * 操作系统
     */
    private String os;

    private String osVersion;

    private String osArch;

    private String osName;

    private String programmingLanguage;

    /**
     * cpu核心数
     */
    private int cpuCore;

    /**
     * 内存大小
     */
    private int memory;

    /**
     * 磁盘大小
     */
    private int disk;

    /**
     * 节点的类型
     */
    private NodeType type;


    private boolean enablePlugin;

}
