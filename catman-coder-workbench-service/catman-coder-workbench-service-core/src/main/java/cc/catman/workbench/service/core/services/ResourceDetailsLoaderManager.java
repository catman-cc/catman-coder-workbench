package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.entity.Resource;

import java.util.Optional;

public interface ResourceDetailsLoaderManager {
    void register(ResourceDetailsLoader loader);

    Object load(Resource resource);

    Object delete(Resource resource);

    /**
     * 根据资源类型查找对应的资源加载器
     * @param resource 资源
     * @return 资源加载器
     */
    Optional<ResourceDetailsLoader> find(Resource resource);
}
