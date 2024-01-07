package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueProviderDefinitionRef {
    @javax.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
    private String id;

    /**
     * 值提取器名称
     */
    private String name;
    /**
     * 值提取器类型
     */
    private String kind;

    /**
     * 值提取器描述
     */
    @Column(name = "`describe`")
    private String describe;

}
