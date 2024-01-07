package cc.catman.coder.workbench.core.type;


/**
 * 泛型类型定义
 */
public class GenericType  extends DefaultType{

    @Override
    public String getTypeName() {
        return "generic";
    }

    @Override
    public boolean isRaw() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isRaw());
    }

    @Override
    public boolean isArray() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isArray());
    }

    @Override
    public boolean isComplex() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isComplex());
    }

    @Override
    public boolean isStruct() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isStruct());
    }

    @Override
    public boolean isRefer() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isRefer());
    }

    @Override
    public boolean isMap() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isMap());
    }

    @Override
    public boolean isSlot() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isSlot());
    }

    @Override
    public boolean isAny() {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isAny());
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return this.privateItems.values().stream().anyMatch(item->item.getType().canConvert(targetType));
    }

    @Override
    public boolean isType(Type target) {
        return this.privateItems.values().stream().anyMatch(item->item.getType().isType(target));
    }
}
