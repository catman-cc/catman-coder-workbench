package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;

public interface ResourceDetailsLoader {

    boolean support(Resource resource);

    /**
     * 资源加载
     * @param resource 资源
     * @return 加载后的资源
     */
    Object load(Resource resource);

    /**
     * 新增一个特定类型的资源
     * @param resource 新增的资源
     * @return 新增后的资源及其详细数据
     */
    ResourceDetails create(ResourceCreate resource);

    default boolean rename(Resource resource,String name){
        return false;
    }

    Object delete(Resource resource);
}
