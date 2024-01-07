package cc.catman.coder.workbench.core.value.template;

import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultValueProviderTemplate implements ValueProviderTemplate {
    /**
     * 获取值提供者模板的唯一标志
     */
    private String id;
    /**
     * 获取值提供者模板的名称
     */
    private String name;
    private String kind;
    private String description;
    private TypeDefinition args;
    private TypeDefinition result;
}
