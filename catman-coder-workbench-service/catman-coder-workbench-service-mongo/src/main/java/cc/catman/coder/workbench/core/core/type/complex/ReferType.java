package cc.catman.coder.workbench.core.core.type.complex;

import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ReferType extends StructType{
    /**
     * 被引用的类型定义信息,引用类型在引用时,同时可以具有自己的类型定义
     * 此时,我们可以根据name的值来覆盖被引用数据的某个值,比如填充slot
     */
    private TypeDefinition typeDefinition;


    @Override
    public boolean isRefer() {
        return true;
    }

}
