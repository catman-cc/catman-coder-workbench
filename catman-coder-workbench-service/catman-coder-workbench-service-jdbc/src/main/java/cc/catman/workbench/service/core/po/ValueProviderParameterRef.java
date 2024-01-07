package cc.catman.workbench.service.core.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * 值提取器配置引用,在实际处理时,会根据引用的id,找到对应的值提取器服务,并由该服务完成配置的保存,加载,删除等操作
 * 但是为了保持引用查找的完整性,当数据具有引用关系,值提取服务必须提供获取引用的方法
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueProviderParameterRef {

    /**
     * 值提取器配置的id
     */
    @javax.persistence.Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "cc.catman.workbench.configuration.id.CatManIdentifierGenerator")
    private String  id;
    /**
     * 所属的值提取器id
     */
    private String belongValueProviderId;
    /**
     * 所属的值提取器类型
     */
    private String belongValueProviderKind;

    /**
     * args 或者 result
     */
    private String fieldName;

    /**
     * 参数的id
     */
    private String parameterId;

    /**
     * 值提取器配置的json字符串,由具体的值提取器服务解析
     */
    private String jsonConfig;
}
