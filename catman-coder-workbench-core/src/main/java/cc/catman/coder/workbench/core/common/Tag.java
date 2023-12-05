package cc.catman.coder.workbench.core.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    /**
     * 标签唯一标志
     */
    private String id;
    /**
     * 标签名称
     */
    private String name;
}
