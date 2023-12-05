package cc.catman.workbench.service.core.po;

/**
 * 标签引用
 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 标签项引用
 */
@Entity
@Table(name = "tag_ref")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRef {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    /**
     * 标签组所属资源的唯一标志
     */
    private String belongId;
    private String kind;
    private String tag;
}
