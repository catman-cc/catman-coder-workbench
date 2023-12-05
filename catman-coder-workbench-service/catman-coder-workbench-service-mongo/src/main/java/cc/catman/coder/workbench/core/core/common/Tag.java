package cc.catman.coder.workbench.core.core.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Tag {
    /**
     * 标签唯一标志
     */
    @MongoId
    private String id;
    /**
     * 标签名称
     */
    private String name;
}
