package cc.catman.workbench.service.core.po.base;

/**
 * 标签引用
 */
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
@Table(name = "tag_ref")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TagRef extends CommonRef {
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;
    /**
     * 标签组所属资源的唯一标志
     */
    private String belongId;
    private String kind;
    private String tag;
}
