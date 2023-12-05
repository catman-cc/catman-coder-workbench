package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.entity.Entity;

/**
 * slot类型表示插槽,需要在配置时将其替换为完整的类型定义
 */
public class SlotType extends DefaultType {
    protected Type slot;

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_SLOT;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return false;
    }

    @Override
    public boolean isSlot() {
        return true;
    }

    @Override
    public boolean isType(Type target) {
        return false;
    }

    @Override
    public Entity<?> toEntity(Object o) {
        return null;
    }
}
