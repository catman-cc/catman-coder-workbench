package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.entity.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Type {

    String getTypeName();

    /**
     * 是不是原始类型
     */
    @JsonIgnore
    default boolean isRaw() {
        return false;
    }

    /**
     * 是不是集合
     */
    @JsonIgnore
    default boolean isArray() {
        return false;
    }

    @JsonIgnore
    default boolean isComplex() {
        return isArray() || isMap() || isStruct() || isRefer();
    }

    /**
     * 是不是结构
     */
    @JsonIgnore
    default boolean isStruct() {
        return false;
    }

    @JsonIgnore
    default boolean isRefer() {
        return false;
    }

    @JsonIgnore
    default boolean isMap() {
        return false;
    }

    @JsonIgnore
    default boolean isSlot() {
        return false;
    }

    @JsonIgnore
    default boolean isAny() {
        return false;
    }

    @JsonIgnore
    default boolean isGeneric() {
        return false;
    }

    @JsonIgnore
    default boolean isAnonymous() {
        return false;
    }

    /**
     * 判断一个类型是否可以转换为另一个类型
     */
    boolean canConvert(Type targetType);

    /**
     * 判断一个类型是否是另一个类型
     */
    @JsonIgnore
    boolean isType(Type target);

    Entity<?> toEntity(Object o);

}
