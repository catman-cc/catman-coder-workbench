package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.parameter.Parameter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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
    @JsonIgnore
    private transient ILoopReferenceContext context=ILoopReferenceContext.create();
}
