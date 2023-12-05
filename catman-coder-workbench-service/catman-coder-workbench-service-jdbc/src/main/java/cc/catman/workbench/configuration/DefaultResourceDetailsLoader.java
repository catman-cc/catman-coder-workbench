package cc.catman.workbench.configuration;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultResourceDetailsLoader implements ResourceDetailsLoader {
    @javax.annotation.Resource
    private ModelMapper modelMapper;
    @Override
    public boolean support(Resource resource) {
        return "resource".equals(resource.getKind());
    }
    @Override
    public Object load(Resource resource) {
        return resource;
    }

    @Override
    public ResourceDetails create(ResourceCreate configuration) {
        return  modelMapper.map(configuration,ResourceDetails.class);
    }

    @Override
    public Object delete(Resource resource) {
        return null;
    }
}
