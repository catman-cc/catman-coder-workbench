package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ResourceRef {
    /**
     * 唯一标志
     */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
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
