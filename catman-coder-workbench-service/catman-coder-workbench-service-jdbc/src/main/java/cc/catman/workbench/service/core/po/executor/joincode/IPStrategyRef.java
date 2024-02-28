package cc.catman.workbench.service.core.po.executor.joincode;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class IPStrategyRef extends CommonRef {
    /**
     * 策略ID
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 是否禁止访问
     */
    private Boolean isDeny;

    /**
     * 所属资源ID
     */
    private String belongId;

    /**
     * 排序
     */
    @Column(name="order_sort")
    private Integer sort;
}
