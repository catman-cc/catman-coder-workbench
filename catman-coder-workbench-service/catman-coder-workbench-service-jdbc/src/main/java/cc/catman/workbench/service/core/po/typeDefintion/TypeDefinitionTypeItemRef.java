package cc.catman.workbench.service.core.po.typeDefintion;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TypeDefinitionTypeItemRef extends CommonRef {
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;

    /**
     * 所属的类型定义
     */
    private String typeDefinitionTypeId;

    /**
     *  类型作为参数的名称
     */
    private String name;

    /**
     * 子项的作用域
     */
    private String itemScope;

    /**
     * 引用的类型定义
     */
    private String referencedTypeDefinitionId;

    /**
     * 类型定义的排序
     */
    private Integer orderIndex;
}
