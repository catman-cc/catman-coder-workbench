package cc.catman.workbench.service.core.po.typeDefintion;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "type_definition")
public class TypeDefinitionPO extends CommonRef {

    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id ;

    /**
     * 类型定义名称
     */
    private String name;

    /**
     * 类型名称
     */
    private String typeName;

    private String scope;

    /**
     * 类型定义
     */
    private String typeId;

    /**
     * 参数的默认值
     */
    protected String defaultValue;

    /**
     * 参数的简短描述
     */
    @Column(name="sort_describe")
    protected String describe;

    @Column(name="is_required")
    protected Boolean required;


    /**
     * mock数据
     */
//    protected Mock mock;
}
