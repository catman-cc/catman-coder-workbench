package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TypeDefinitionTypeItemRef {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    /**
     * 所属的类型定义
     */
    private String typeDefinitionTypeId;

    /**
     * 引用的类型定义
     */
    private String referencedTypeDefinitionId;

    /**
     * 类型定义的排序
     */
    private int orderIndex;
}
