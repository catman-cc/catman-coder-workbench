package cc.catman.workbench.service.core.po.resource;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResourceRef extends CommonRef {
    /**
     * 唯一标志
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String kind;

    /**
     * 资源id,配合kind使用
     */
    private String resourceId;

    /**
     * 资源所属的上级资源
     */
    private String parentId;

    /**
     * 如果资源为leaf资源,则表示该资源无法再继续向下遍历
     */
    private Boolean leaf;

    @Column(length = 1024*1024*20)
    private String extra;

    private String previousId;

    private String nextId;
}
