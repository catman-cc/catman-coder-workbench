package cc.catman.workbench.configuration;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FunctionResourceDetailsLoader implements ResourceDetailsLoader {
   @jakarta.annotation.Resource
    private ModelMapper modelMapper;
    @Override
    public boolean support(Resource resource) {
        return "function".equals(resource.getKind());
    }

    @Override
    public Object load(Resource resource) {
        return null;
    }

    @Override
    public ResourceDetails create(ResourceCreate resource) {

        resource.setLeaf(true);
        return modelMapper.map(resource,ResourceDetails.class);
    }

    @Override
    public Object delete(Resource resource) {
        return null;
    }
}
