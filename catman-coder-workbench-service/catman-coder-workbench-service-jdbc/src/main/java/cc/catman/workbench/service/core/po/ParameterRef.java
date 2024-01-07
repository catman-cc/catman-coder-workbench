package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class ParameterRef {

    @javax.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
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


    private String defaultValueProviderDefinitionId;


    private boolean required;


}
