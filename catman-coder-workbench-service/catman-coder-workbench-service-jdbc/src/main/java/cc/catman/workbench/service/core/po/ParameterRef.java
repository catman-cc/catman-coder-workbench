package cc.catman.workbench.service.core.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;

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
    protected String describe;

}
