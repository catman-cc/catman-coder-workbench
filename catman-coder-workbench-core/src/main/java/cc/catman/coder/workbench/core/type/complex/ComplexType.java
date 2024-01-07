package cc.catman.coder.workbench.core.type.complex;

import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 复合类型定义,里面会包含一组类型定义信息
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public  class ComplexType extends DefaultType {

    /**
     * 被继承的类型定义信息
     */
    private TypeDefinition extend;

    protected List<TypeDefinition> overwriteItems = new ArrayList<>();

    @Override
    public boolean isRaw() {
        return false;
    }
}
