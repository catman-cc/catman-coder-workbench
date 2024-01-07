package cc.catman.workbench.api.server.configuration.resources;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.entity.StringConfig;
import cc.catman.workbench.service.core.services.IStringConfigService;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


public class HttpValueProviderQuickerResourceDetailsLoader implements ResourceDetailsLoader {
    @javax.annotation.Resource
    private ModelMapper modelMapper;

    @javax.annotation.Resource
    private ObjectMapper objectMapper;

    @javax.annotation.Resource
    private IStringConfigService stringConfigService;

    @Override
    public boolean support(Resource resource) {
        return "HttpValueProviderQuicker".equals(resource.getKind());
    }

    @Override
    public Object load(Resource resource) {
        String resourceId = resource.getResourceId();
        return stringConfigService.findById(resourceId).orElse(null);
    }

    @Override
    @SneakyThrows
    public ResourceDetails create(ResourceCreate resource) {

        StringConfig sc = StringConfig.builder()
                .belongId(resource.getId())
                .value(objectMapper.writeValueAsString(resource.getConfig()))
                .build();
        sc=stringConfigService.save(sc);
        resource.setResourceId(sc.getId());
        ResourceDetails rd = modelMapper.map(resource, ResourceDetails.class);
        rd.setDetails(sc);
        return rd;
    }

    @Override
    public Object delete(Resource resource) {
        return stringConfigService.deleteById(resource.getResourceId()).orElse(null);
    }

    @Override
    public boolean rename(Resource resource, String name) {
       return true;
    }
}
