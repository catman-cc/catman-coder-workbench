package cc.catman.workbench.service.core.po.parameter;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParameterRef extends CommonRef {

    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id ;

    /**
     * 类型定义名称
     */
    private String name;

    private String scope;
    /**
     * 类型定义
     */
    private String typeDefinitionId;

    /**
     * 参数的默认值
     */
    protected String defaultValue;

    /**
     * 参数的简短描述
     */
    protected String sortDescribe;

    /**
     * 参数的简短描述
     */
    @Column(name = "`describe`")
    protected String describe;


    private String valueProviderDefinitionId;

    private String valueFunctionCallInfoId;


    private String defaultValueProviderDefinitionId;

    private String defaultValueFunctionCallInfoId;


    private boolean required;


}
