package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProvider;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
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
    private ValueProvider<?> value;

    /**
     * 参数的默认值
     */
    private ValueProvider<?> defaultValue;


    @Builder.Default
    private List<Parameter> items = new ArrayList<>();
}
