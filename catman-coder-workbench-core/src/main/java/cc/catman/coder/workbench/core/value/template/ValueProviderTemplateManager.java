package cc.catman.coder.workbench.core.value.template;

import cc.catman.coder.workbench.core.type.TypeDefinition;

import java.util.List;

public interface ValueProviderTemplateManager {

    List<ValueProviderTemplate> search(String keyword);

    List<ValueProviderTemplate> findTemplatesByArgs(TypeDefinition args);

    List<ValueProviderTemplate> findTemplatesByResult(TypeDefinition result);

    List<ValueProviderTemplate> findTemplatesByArgsAndResult(TypeDefinition args, TypeDefinition result);

    List<ValueProviderTemplate> findTemplatesByKind(String kind);

    ValueProviderTemplateManager register(ValueProviderTemplate template);

}
