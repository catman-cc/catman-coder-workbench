package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "string_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringConfigPO {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String belongId;

    /**
     * 一个被序列化为字符串的对象,具体的序列化方法由持有者决定
     */
    private String value;
}
