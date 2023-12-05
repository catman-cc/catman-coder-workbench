package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueProviderConfigItemRef {
    /**
     * 值提取器配置子项的id
     */
    @javax.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
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
