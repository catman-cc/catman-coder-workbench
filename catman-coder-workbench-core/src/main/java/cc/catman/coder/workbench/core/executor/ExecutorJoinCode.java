package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.common.IPStrategy;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExecutorJoinCode extends Base {
    /**
     * 接入码ID
     */
    private String id;

    /**
     * 和ID类似,但不是唯一的,可以重复,场景: 刷新接入码
     */
    private String key;

    /**
     * 接入码类型
     */
    private String kind;

    /**
     * 接入码支持的类型
     */
    private List<String> supportedKinds;

    /**
     * 接入码名称
     */
    private String name;

    /**
     * 接入码
     */
    private String code;


    /**
     * 接入码是否可重复使用,示例:
     * - A使用接入码,接入成功
     * - B使用接入码,是否替换掉A
     */
    private Boolean repeatable;

    /**
     * 接入码重复使用策略,默认为DENY
     */
    private EJoinCodeRepeatStrategy repeatStrategy;

    /**
     * 最大可重复使用次数,-1表示无限制
     */
    private Integer maxRepeatable;



    /**
     * 是否限制允许连接的时间
     */
    private Boolean limitAccessTime;
    /**
     * 接入后,允许访问的时间,单位毫秒,-1表示无限制
     * 执行协调器会根据这个时间来判断是否允许访问
     */
    private Long allowedAccessTime;

    /**
     * 在指定时间后,允许接入
     */
    private Long allowAccessStartTime;

    /**
     * 在指定时间后,禁止接入
     */
    private Long allowAccessEndTime;

    /**
     * 是否限制允许连接的IP地址
     */
    private Boolean limitAccessIps;

    /**
     * ip过滤策略
     */
    @Builder.Default
    private List<IPStrategy> ipFilter = new ArrayList<>();

    /**
     * 接入码是否已失效,如果失效,则不允许再使用
     */
    private Boolean invalid;

    /**
     * 接入码失效原因
     */
    private String invalidReason;

    /**
     * 接入码状态
     */
    private ExecutorJoinCodeStatus status;
}
