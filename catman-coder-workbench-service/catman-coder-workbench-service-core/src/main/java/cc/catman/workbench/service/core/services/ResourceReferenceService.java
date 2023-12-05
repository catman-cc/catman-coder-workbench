package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.entity.Resource;

import java.util.List;

public interface ResourceReferenceService {
    /**
     * 检查一个资源是否引用另一个资源
     * @param resource 资源
     * @param reference 引用的资源
     * @return 是否引用
     */
    boolean checkReference(Resource resource,Resource reference);

    /**
     * 检查一个资源是否被其他资源引用
     * @param resource 资源
     * @return 是否被引用
     */
    boolean checkReference(Resource resource);

    /**
     * 获取所有直接引用了某个资源的资源集合
     * @param resource 资源
     * @return 引用了该资源的资源集合
     */
    List<Resource> getReference(Resource resource);

    /**
     * 获取所有间接引用了某个资源的资源集合,包含直接引用
     * @param resource 资源
     * @return 间接引用了该资源的资源集合
     */
    List<Resource> getIndirectReference(Resource resource);
}
