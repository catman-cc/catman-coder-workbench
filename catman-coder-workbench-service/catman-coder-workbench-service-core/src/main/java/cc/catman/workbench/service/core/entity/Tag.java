package cc.catman.workbench.service.core.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    /**
     * 标签唯一标志
     */
    private String id;
    /**
     * 标签名称
     */
    private String name;

    public cc.catman.coder.workbench.core.common.Tag convert(){
        return cc.catman.coder.workbench.core.common.Tag.builder()
                .id(id)
                .name(name)
                .build();
    }
}
