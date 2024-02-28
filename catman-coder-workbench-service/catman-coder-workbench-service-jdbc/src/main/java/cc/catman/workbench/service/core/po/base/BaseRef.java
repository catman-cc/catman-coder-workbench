package cc.catman.workbench.service.core.po.base;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "metadata")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BaseRef extends CommonRef {
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;
    /**
     * 被标注的资源id
     */
    private String belongId;
    /**
     * id
     */
    private String groupId;

    /**
     * 被标注的资源类型,通过冗余该字段,减少筛选次数
     */
    private String kind;

    private String scope;
//
//    /**
//     * 标签组唯一标识
//     */
//    private String labelsId;
//
//    private String tagId;

    private String wiki;
}
