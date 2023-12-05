package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRef {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    /**
     * 被标注的资源id
     */
    private String belongId;
    /**
     * id
     */
    private String groupId;

    /**
     * 被标注的资源类型,通过冗余该字段,减少筛选次数
     */
    private String kind;

    private String scope;
//
//    /**
//     * 标签组唯一标识
//     */
//    private String labelsId;
//
//    private String tagId;

    private String wiki;
}
