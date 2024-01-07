package cc.catman.coder.workbench.core.type.complex;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.type.Type;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 一个引用类型的类型定义,支持引用其他类型定义
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReferType extends StructType{
    /**
     * 被引用的类型定义信息,引用类型在引用时,同时可以具有自己的类型定义
     * 此时,我们可以根据name的值来覆盖被引用数据的某个值,比如填充slot
     */
    private TypeDefinition typeDefinition;

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_REFER;
    }

    @Override
    public boolean isRefer() {
        return true;
    }

    @Override
    public boolean canConvert(Type target) {
        if (target.isAny()){
            return true;
        }
        return this.typeDefinition.getType().canConvert(target);
    }
}
