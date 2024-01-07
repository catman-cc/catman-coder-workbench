package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.entity.Entity;

/**
 * 任意类型,可以转换为任意类型,在一定程度上等同于java的Object
 * 涉及该类型的目的是为了解决在解析过程中,无法确定类型的情况
 * 同时有些参数不需要进行类型限制,比如map的value
 */
public class AnyType extends DefaultType{
    @Override
    public String getTypeName() {
        return "any";
    }
    @Override
    public boolean canConvert(Type targetType) {
        return true;
    }

    @Override
    public boolean isSlot() {
        return false;
    }

    @Override
    public boolean isType(Type target) {
        return true;
    }

    @Override
    public boolean isRefer() {
        return true;
    }

    @Override
    public boolean isStruct() {
        return true;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    public boolean isRaw() {
        return true;
    }

    public boolean isMap() {
        return true;
    }



    public boolean isAny() {
        return true;
    }
    public Entity<?> toEntity(Object o) {
        return null;
    }


}
