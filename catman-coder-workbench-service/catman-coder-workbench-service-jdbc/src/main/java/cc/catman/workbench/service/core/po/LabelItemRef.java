package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 标签项引用
 */
@Entity
@Table(name = "label_items_ref")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelItemRef {
    /**
     * 标签项id
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
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
