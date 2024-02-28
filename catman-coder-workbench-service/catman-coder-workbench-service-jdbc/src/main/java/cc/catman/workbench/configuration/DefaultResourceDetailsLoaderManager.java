package cc.catman.workbench.configuration;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import cc.catman.workbench.service.core.services.ResourceDetailsLoaderManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DefaultResourceDetailsLoaderManager implements ResourceDetailsLoaderManager{


   @jakarta.annotation.Resource
    private List<ResourceDetailsLoader> resourceDetailsLoaderManagers;

    @Override
    public void register(ResourceDetailsLoader loader) {

    }

    @Override
    public Object load(Resource resource) {
       return resourceDetailsLoaderManagers.stream()
                .filter(loader -> loader.support(resource))
                .findFirst()
                .map(loader -> loader.load(resource))
                .orElse(null);
    }

    @Override
    public Object delete(Resource resource) {
        return find(resource).map(loader -> loader.delete(resource)).orElse(null);
    }

    @Override
    public Optional<ResourceDetailsLoader> find(Resource resource) {
        return resourceDetailsLoaderManagers.stream()
                .filter(loader -> loader.support(resource))
                .findFirst();
    }
}
