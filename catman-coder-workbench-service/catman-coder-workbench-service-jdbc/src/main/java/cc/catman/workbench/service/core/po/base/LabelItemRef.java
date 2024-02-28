package cc.catman.workbench.service.core.po.base;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

/**
 * 标签项引用
 */
@Entity
@Table(name = "label_items_ref")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LabelItemRef extends CommonRef {
    /**
     * 标签项id
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;

    /**
     * 所属数据
     */
    private String belongId;

    /**
     * 所属的资源类型,通过冗余该字段,减少筛选次数
     */
    private String kind;

    /**
     * 标签项名称
     */
    private String name;

    /**
     *  标签项值
     */
    private String value;

    /**
     * 排序
     */
    private int sorting;
}
