package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.value.template.ValueProviderTemplateManager;

/**
 *  值提取器模板服务,
 *  这里需要注意的是, 根据kind查找可以查找到多个针对指定kind的模板
 *
 *  为了简化调用,对模板进行分组,比如,和代码绑定的模板被标记为核心模板,
 *  而和代码无关的模板被标记为业务模板
 */
public interface IValueProviderDefinitionTemplateService extends ValueProviderTemplateManager {

}
