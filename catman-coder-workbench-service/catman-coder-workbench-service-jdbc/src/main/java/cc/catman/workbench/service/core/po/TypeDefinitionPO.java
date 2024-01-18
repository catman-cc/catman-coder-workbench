package cc.catman.workbench.service.core.po;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "type_definition")
public class TypeDefinitionPO {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
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



    /**
     * mock数据
     */
//    protected Mock mock;
}
