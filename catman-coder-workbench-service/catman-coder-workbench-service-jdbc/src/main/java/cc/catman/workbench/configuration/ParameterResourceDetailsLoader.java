package cc.catman.workbench.configuration;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.parameter.ParameterSchema;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.workbench.service.core.Constants;
import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.IParameterService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParameterResourceDetailsLoader implements ResourceDetailsLoader {

   @jakarta.annotation.Resource
    private ModelMapper modelMapper;

   @jakarta.annotation.Resource
    private IParameterService parameterService;

   @jakarta.annotation.Resource
    private ITypeDefinitionService typeDefinitionService;

    @Override
    public boolean support(Resource resource) {
        return Constants.ResourceKind.Parameter.equals(resource.getKind());
    }

    @Override
    public Object load(Resource resource) {
        return parameterService.findById(resource.getResourceId())
                .map(ParameterSchema::of)
                .orElse(null);
    }

    @Override
    public ResourceDetails create(ResourceCreate resource) {
        String name = Optional.ofNullable(resource.getName()).orElseGet(() -> "newParameter");
        // 创建资源有两种形态,其中一种是从现有的类型定义中创建
        Parameter parameter = readTypeDefinition(resource)
                .map(typeDefinition -> {
                    // 从类型定义中创建参数定义
                    return parameterService.create(typeDefinition);
                })
                .orElseGet(() -> Parameter.builder()
                        .type(TypeDefinition.builder()
                                .name(name)
                                .type(new StringRawType())
                                .build()).build());

        parameter.setName(name);
        parameter = parameterService.save(parameter);
        ResourceDetails details = modelMapper.map(resource, ResourceDetails.class);
        details.setResourceId(parameter.getId());
        details.setName(parameter.getName());
        details.setLeaf(true);
        details.setDetails(parameter);
        // 另一种是直接创建一个新的类型定义
        return details;
    }

    @Override
    public Object delete(Resource resource) {
        return null;
    }

    public Optional<TypeDefinition> readTypeDefinition(ResourceCreate resource) {
        return Optional.ofNullable(resource.getConfig().get("tid"))
                .filter(tid -> tid instanceof String)
                .filter(tid -> !((String) tid).isBlank())
                .flatMap(tid -> typeDefinitionService.findById(tid.toString()));
    }
}
