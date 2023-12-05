package cc.catman.workbench.service.core.entity;

import lombok.Data;


/**
 * 快照信息,当数据不生效时,将会进行前置操作快照处理
 */
@Data
public class Snapshot {
    private String id;

    /**
     * 快照对应资源的唯一标志
     */
    private String belongId;

    /**
     * 快照对应的资源类型
     */
    private String kind;

    /**
     * 快照名称,如果用户没有设置,则为空
     */
    private String name;

    /**
     * 以json格式存储的数据资源
     */
    private String jsonValue;

    /**
     * 快照对应的资源最后更新时间
     */
    private long lastUpdateTime;

    /**
     * 快照的版本,应保持递增关系
     */
    private long version;

}
