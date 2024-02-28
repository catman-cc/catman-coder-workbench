package cc.catman.workbench.service.core.po.executor.joincode;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;



@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExecutorJoinCodeRef extends CommonRef {
    /**
     * 接入码ID
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;
    /**
     * 和ID类似,但不是唯一的,可以重复,场景: 刷新接入码
     */
    @Column(name = "code_group")
    private String key;

    /**
     * 接入码类型
     */
    private String kind;

    /**
     * 接入码支持的类型,多个类型用逗号分隔
     */
    private String supportedKinds;


    /**
     * 接入码名称
     */
    private String name;

    /**
     * 接入码
     */
    private String code;

    /**
     * 接入码是否已禁用
     */
    private Boolean disabled;

    /**
     * 接入码是否可重复使用,示例:
     *  - A使用接入码,接入成功
     *  - B使用接入码,是否替换掉A
     */
    private Boolean repeatable;


    /**
     * 接入码重复使用策略,默认为DENY
     */
    private String repeatStrategy;

    /**
     *
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
    private  Long allowedAccessTime;

    /**
     * 在指定时间后,允许接入
     */
    private Long allowAccessStartTime;

    /**
     * 是否限制允许连接的IP地址
     */
    private Boolean limitAccessIps;
    /**
     * 在指定时间后,禁止接入
     */
    private Long allowAccessEndTime;

    /**
     * 允许访问的IP地址,多个IP地址用逗号分隔
     */
    private String allowedAccessIps;

    /**
     * 禁止访问的IP地址,多个IP地址用逗号分隔
     */
    private String deniedAccessIps;

    /**
     * 接入码是否已失效,如果失效,则不允许再使用
     */
    private Boolean invalid;

    /**
     * 接入码失效原因
     */
    private String  invalidReason;

    @Column(name="status_value")
    private String status;
}
