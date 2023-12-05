package cc.catman.coder.workbench.core.common;

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
     * 所属的父分组
     */
    private Group parent;
}
