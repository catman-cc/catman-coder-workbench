package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.entity.Resource;

import java.util.List;
import java.util.function.Consumer;

/**
 * 资源接口服务
 */
public interface IResourceService {
     String RESOURCE_ROOT_ID = "__root_1";
     String RESOURCE_ROOT_NAME = "root";
     String RESOURCE_KIND = "resource";
    /**
     * 获取资源树
     *
     * @param id    资源id
     * @param depth 深度,如果为0,则只返回当前资源,如果为1,则返回当前资源以及子资源,如果为-1,则返回所有子资源
     * @return 资源树
     */
    Resource findById(String id, int depth);

    /**
     * 根据资源对应的kind和resourceId查询资源
     *
     * @param kind       资源类型
     * @param resourceId 资源id
     * @return 资源
     */
    Resource findByKindAndResourceId(String kind, String resourceId);

    List<Resource> listByParentId(String parentId);

    List<Resource> listByParentId(String parentId, int depth);

    /**
     * 获取跟资源
     *
     * @param depth 深度,如果为0,则只返回当前资源,如果为1,则返回当前资源以及子资源,如果为-1,则返回所有子资源
     * @return 资源树
     */
    Resource findRoot(int depth);

    /**
     * 获取资源树
     */
    Resource findRoot();

    /**
     * 保存资源,也包括更新操作
     *
     * @param resource 资源
     * @return 保存后的资源
     */
    Resource save(Resource resource);

    /**
     * 更新资源
     *
     * @param resource 资源
     * @return 更新后的资源
     */
    Resource update(Resource resource);

    default Resource rename(String id, String name) {
        if (RESOURCE_ROOT_ID.equals(id)) {
            throw new RuntimeException("root resource can not be renamed");
        }
        Resource res = findById(id, 0);
        res.setName(name);
        return update(res);
    }

    /**
     * 根据id删除资源
     *
     * @param id 资源id
     */
    void deleteById(String id);

    void deepWithHandler(String id, Consumer<Resource> handler);

    void move(String id, String parentId, String previousId,String nextId,Integer index);

    boolean flushSortByParentId(String parentId,boolean deep);
}
