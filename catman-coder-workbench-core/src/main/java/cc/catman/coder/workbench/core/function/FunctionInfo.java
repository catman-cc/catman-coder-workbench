package cc.catman.coder.workbench.core.function;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 函数定义
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfo extends Base {
    /**
     * 唯一标志
     */
    private String  id;
    /**
     * 函数名称
     */
    private String name;
    /**
     * 函数类型
     */
    private String kind;

    /**
     * 函数的简短描述
     */
    private String describe;
    /**
     * 函数的参数
     */
    private TypeDefinition args;
    /**
     * 函数的返回值
     */
    private TypeDefinition returns;
}
