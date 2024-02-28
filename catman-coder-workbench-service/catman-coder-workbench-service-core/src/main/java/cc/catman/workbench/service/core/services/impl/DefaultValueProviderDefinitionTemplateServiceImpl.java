package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.template.ValueProviderTemplate;
import cc.catman.coder.workbench.core.value.template.ValueProviderTemplateManager;
import cc.catman.workbench.service.core.services.IValueProviderDefinitionTemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultValueProviderDefinitionTemplateServiceImpl implements IValueProviderDefinitionTemplateService {
    @Resource
    private ValueProviderTemplateManager manager;
    @Override
    public ValueProviderTemplateManager register(ValueProviderTemplate template) {
      return manager.register(template);
    }
    @Override
    public List<ValueProviderTemplate> search(String keyword) {
       return manager.search(keyword);
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByArgs(TypeDefinition args) {
     return manager.findTemplatesByArgs(args);
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByResult(TypeDefinition result) {
      return manager.findTemplatesByResult(result);
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByArgsAndResult(TypeDefinition args, TypeDefinition result) {
       return manager.findTemplatesByArgsAndResult(args,result);
    }

    @Override
    public List<ValueProviderTemplate> findTemplatesByKind(String kind) {
       return manager.findTemplatesByKind(kind);
    }
}
