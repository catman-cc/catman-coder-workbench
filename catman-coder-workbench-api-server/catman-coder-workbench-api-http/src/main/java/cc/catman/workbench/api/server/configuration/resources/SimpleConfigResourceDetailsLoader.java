package cc.catman.workbench.api.server.configuration.resources;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SimpleConfigResourceDetailsLoader implements ResourceDetailsLoader {
   @jakarta.annotation.Resource
    private ModelMapper modelMapper;
    @Override
    public boolean support(Resource resource) {
        return "HttpValueProviderQuicker".equals(resource.getKind());
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
