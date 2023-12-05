package cc.catman.workbench.service.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private String id;
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 名称
     */
    private String name;
    /**
     * 所属的父分组id
     */
    private Group parent;

    public cc.catman.coder.workbench.core.common.Group convert() {
        return cc.catman.coder.workbench.core.common.Group.builder()
                .id(id)
                .namespace(namespace)
                .name(name)
                .parent(parent == null ? null : parent.convert())
                .build();
    }
}
