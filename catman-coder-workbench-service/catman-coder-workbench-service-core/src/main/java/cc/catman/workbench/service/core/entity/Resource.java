package cc.catman.workbench.service.core.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 树形结构,主要用于遍历资源树.
 * 此处只包含了最基础的数据定义,关于每个资源的细节,需要调用对应的资源接口获取
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends Base{
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
     * 限制创建的子资源类型,多个类型用逗号分隔,如果为空,则表示不限制
     */
    private List<String> supportedChildKinds;

    /**
     * 如果资源为leaf资源,则表示该资源无法再继续向下遍历
     */
    private boolean leaf;

    /**
     * 子资源
     */
    private List<Resource> children;

    /**
     * 资源配置,也可以直接存储一些简单资源的配置信息
     */
    private String extra;

    /**
     * 资源前面的资源,如果资源前面的资源不存在,则表示该资源是第一个资源
     */
    private String previousId;

    private String nextId;

    public void addChildren(Resource resource) {
       if (children == null) {
           children = new ArrayList<>();
       }
        this.children.add(resource);
    }

    public void filter(Function<Resource,Boolean> filter, Consumer<Resource> consumer) {
        if (filter.apply(this)) {
            consumer.accept(this);
        }

        if (this.children != null) {
            this.children.forEach(child -> {
                child.filter(filter, consumer);
            });
        }
    }
    public Resource filterAndNotEmpty(Function<Resource,Boolean> filter, ModelMapper modelMapper) {
        if (this.isLeaf()){
            // 如果是叶子节点,则不再继续向下遍历
            if (filter.apply(this)) {
               // 叶子节点通过了过滤器,则直接返回
                return modelMapper.map(this,Resource.class);
            }else {
                return null;
            }
        }
        // 当前不是叶子节点,则继续向下遍历
        List<Resource> crs = this.children.stream().map(child -> child.filterAndNotEmpty(filter, modelMapper)).filter(Objects::nonNull).toList();
        if (crs.isEmpty()){
            return null;
        }
        Resource resource = modelMapper.map(this, Resource.class);
        resource.setChildren(crs);
        return resource;
    }
}
