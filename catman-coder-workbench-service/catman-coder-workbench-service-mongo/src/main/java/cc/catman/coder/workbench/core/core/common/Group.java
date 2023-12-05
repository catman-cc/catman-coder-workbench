package cc.catman.coder.workbench.core.core.common;

import cc.catman.coder.workbench.core.core.type.IsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("group")
@IsType
public class Group {
    @Id
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
