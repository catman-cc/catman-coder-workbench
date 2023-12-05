package cc.catman.workbench.service.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形结构,主要用于遍历资源树.
 * 此处只包含了最基础的数据定义,关于每个资源的细节,需要调用对应的资源接口获取
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    /**
     * 唯一标志
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String kind;

    /**
     * 资源id,配合kind使用
     */
    private String resourceId;

    private String parentId;

    /**
     * 如果资源为leaf资源,则表示该资源无法再继续向下遍历
     */
    private boolean leaf;

    /**
     * 子资源
     */
    private List<Resource> children;

    public void addChildren(Resource resource) {
       if (children == null) {
           children = new ArrayList<>();
       }
        this.children.add(resource);
    }
}
