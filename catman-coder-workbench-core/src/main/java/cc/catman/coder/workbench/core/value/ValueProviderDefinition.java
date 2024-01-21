package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 值提取器定义
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ValueProviderDefinition extends Base {
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
    private String describe;

    private Scope scope;

    /**
     * 值提取器配置
     */
    private Parameter args;

    /**
     * 值提取器结果
     */
    private Parameter result;

    private String config;

    /**
     *  前置值提取器
     *  在处理时,将会优先处理,并使用其name作为变量名存储到上下文中
     */
    @Builder.Default
    private List<ValueProviderDefinition> preValueProviders=new ArrayList<>();

    /**
     *  后置值提取器
     */
    @Builder.Default
    private List<ValueProviderDefinition> postValueProviders=new ArrayList<>();

    @Builder.Default
    private Map<String,ValueProviderDefinition> publicValueProviderDefinitions=new HashMap<>();

    @Builder.Default
    private Map<String,Parameter> publicParameters=new HashMap<>();

    @Builder.Default
    private Map<String, TypeDefinition> publicTypeDefinitions=new HashMap<>();
}
