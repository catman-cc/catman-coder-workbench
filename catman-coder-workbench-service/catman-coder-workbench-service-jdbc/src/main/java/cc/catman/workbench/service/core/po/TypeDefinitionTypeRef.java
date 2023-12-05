package cc.catman.workbench.service.core.po;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 类型定义的引用关系,用于查询类型定义的引用关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "type_definition_type_ref")
public class TypeDefinitionTypeRef {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    /**
     * 所属的类型定义
     */
    private String typeDefinitionId;

    /**
     * 类型名称
     */
    protected String typeName;
}
