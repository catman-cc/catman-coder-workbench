package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 参数定义
 * 一个参数定义的结构取决于他所依赖的类型定义
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Parameter extends Base {

    @Builder.Default
    private String id = Constants.TEMP_ID_SUFFIX+UUID.randomUUID().toString();

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数的简短描述
     */
    protected String describe;


    /**
     * 参数的类型定义
     */
    private TypeDefinition type;

    /**
     * 参数的值
     */
    private ValueProviderDefinition value;


    /**
     * 参数的默认值
     */
    private ValueProviderDefinition defaultValue;


    /**
     * 参数是否填项,如果当前参数是必填项,在解析时,会进行验证
     */
    private boolean required;

    /**
     *  是否跳过子节点的解析,该参数可以在前端进行设置
     *
     *  如果当前参数是一个对象,并且该属性为true,则不会解析该对象的子节点
     *  同时,如果一个参数的所有子节点都是用了父节点取值器,那么该属性也会被设置为true
     */
    private boolean skipChildFlag;



    @Builder.Default
    private List<Parameter> items = new ArrayList<>();

    public Optional<Parameter> get(String name){
        return items.stream().filter(parameter -> parameter.getName().equals(name)).findFirst();
    }

    public Parameter addItem(Parameter parameter){
        items.add(parameter);
        return this;
    }
}
