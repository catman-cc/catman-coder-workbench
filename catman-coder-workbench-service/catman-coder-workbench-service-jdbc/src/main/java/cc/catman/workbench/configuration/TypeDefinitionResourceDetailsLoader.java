package cc.catman.workbench.configuration;

import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.type.AnonymousType;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TypeDefinitionResourceDetailsLoader implements ResourceDetailsLoader {
    @javax.annotation.Resource
    private ModelMapper modelMapper;
    @javax.annotation.Resource
    private ITypeDefinitionService typeDefinitionService;
    @Override
    public boolean support(Resource resource) {
        return "td".equals(resource.getKind());
    }

    @Override
    public Object load(Resource resource) {
        return typeDefinitionService.findById(resource.getResourceId());
    }

    @Override
    public ResourceDetails create(ResourceCreate resource) {
        Object type = resource.getConfig().get("type");
        String name=Optional.ofNullable(resource.getName()).orElseGet(()->"new type definition");
        TypeDefinition typeDefinition = TypeDefinition.builder()
                .name(name)
                .scope(Scope.PUBLIC)
                .type(new AnonymousType(String.valueOf(type)))
                .build();
        TypeDefinition save = typeDefinitionService.save(typeDefinition);
        ResourceDetails details=modelMapper.map(resource,ResourceDetails.class);
        details.setResourceId(save.getId());
        details.setName(name);
        details.setLeaf(true);
        details.setDetails(save);
        return details;
    }

    @Override
    public Object delete(Resource resource) {
        return typeDefinitionService.deleteById(resource.getResourceId()).orElse(null);
    }
}
