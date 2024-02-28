package cc.catman.workbench.service.core.po.valueProvider;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.po.CommonRef;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueProviderConfigItemRef extends CommonRef {
    /**
     * 值提取器配置子项的id
     */
    @jakarta.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", type = CatManIdentifierGenerator.class)
    private String id;

    /**
     * 所属的值提取器配置id
     */
    private String valueProviderConfigId;

    /**
     * 所属的值提取器配置中的字段名
     */
    private String fieldName;

    /**
     * 如果引用了其他的值提取器,则需要指定引用的值提取器的id
     */
    private String referencedValueProviderId;

    /**
     *  当字段是一个map或者复合对象时,需要指定字段的名称
     */
    private String name;

    /**
     * 当字段是一个集合时,需要指定集合的索引
     */
    private int orderIndex;

    public boolean isFiled(String name){
        return this.fieldName.equals(name);
    }

}
