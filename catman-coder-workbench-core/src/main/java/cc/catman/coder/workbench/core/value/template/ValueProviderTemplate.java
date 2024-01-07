package cc.catman.coder.workbench.core.value.template;

import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderFactory;

/**
 * 值提供者模板,
 * 值提取器和模板不是一一对应的关系,一个值提取器可以有多个模板,
 * 值提取器是执行业务的实体,而模板是值提取器的一种配置方式
 * 通过模板可以提供一个值提取器的配置界面,并且可以通过模板创建值提取器
 * 同时用户可以创建业务模板,并且可以通过业务模板创建值提取器
 */

public interface ValueProviderTemplate {
    /**
     * 获取值提供者模板的唯一标志
     */
    String getId();

    /**
     * 获取值提供者模板的名称
     */
    String getName();

    /**
     * 获取值提供者模板的唯一类型标志
     */
    String getKind();

    /**
     * 获取值提供者模板的描述信息
     */
    String getDescription();

    /**
     * 获取值提供者模板的入参类型定义
     */
    TypeDefinition getArgs();

    /**
     * 获取值提供者模板的返回值类型定义
     */
    TypeDefinition getResult();

}
