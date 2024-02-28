package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.services.IResourceService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import cc.catman.workbench.service.core.services.ResourceReferenceService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TypeDefinitionResourceReferenceServiceImpl implements ResourceReferenceService {
   @jakarta.annotation.Resource
    private ITypeDefinitionService typeDefinitionService;
   @jakarta.annotation.Resource
    private IResourceService  resourceService;

    @Override
    public boolean checkReference(Resource resource, Resource reference) {
       return typeDefinitionService.findById(reference.getResourceId())
               .map(referenceTypeDefinition ->
                       typeDefinitionService.findById(resource.getResourceId())
                               .map(typeDefinition -> typeDefinition.recursionListPublic().stream()
                                       .anyMatch(td -> td.getId().equals(referenceTypeDefinition.getId())))
                               .orElse(false))
               .orElse(false);
    }

    @Override
    public boolean checkReference(Resource resource) {
        // 获取资源对应的类型定义
       return typeDefinitionService.findById(resource.getResourceId())
               .map(typeDefinition -> typeDefinition.recursionListPublic().size() > 0)
               .orElse(false);
    }

    @Override
    public List<Resource> getReference(Resource resource) {
        return typeDefinitionService.findDirectReferencedById(resource.getResourceId()).stream()
                .map(typeDefinition -> resourceService.findByKindAndResourceId("td", typeDefinition.getId()))
                .toList();
    }

    @Override
    public List<Resource> getIndirectReference(Resource resource) {
        return typeDefinitionService.findById(resource.getResourceId())
                .map(typeDefinition ->
                        typeDefinition.recursionListPublic()
                                .stream()
                                .map(td-> resourceService.findByKindAndResourceId("td", td.getId()))
                                .toList())
                .orElse(Collections.emptyList());
    }
}
