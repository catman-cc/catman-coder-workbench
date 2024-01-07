package cc.catman.coder.workbench.core.type.complex;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.entity.ListEntity;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.Type;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.TypeUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * 数据类型,在定义时,可以定义多个不同的元素,
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArrayType extends ComplexType {

    public static final String ITEMS = "items";

    public static final String ELEMENT_NAME = "elements";

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_ARRAY;
    }

    public Type getElement() {
        return privateItems.size() > 0 ? privateItems.get(0).getType() : null;
    }

    public ArrayType setElement(DefaultType type) {
        String id= UUID.randomUUID().toString();
        this.privateItems.put(id,TypeDefinition.builder()
                        .id(id)
                .name(ELEMENT_NAME)
                .type(type)
                .build());
        return this;
    }

    @Override
    public boolean canConvert(Type targetType) {
        if (targetType.isAny()){
            return true;
        }
        if (!targetType.isArray()) {
            return false;
        }

        if (this.getElement() == null) {
            return false;
        }
        if (targetType instanceof ArrayType tt) {
            return this.getElement().canConvert(tt.getElement());
        }
        return false;
    }

    @Override
    public boolean isType(Type target) {
        if (target.isAny()){
            return true;
        }
        return getTypeName().equals(target.getTypeName());
    }

    @Override
    public ListEntity toEntity(Object obj) {
        // 支持数组和集合
        ListEntity entity = new ListEntity(this);

        Optional.ofNullable(obj)
                .filter(o -> o.getClass().isArray() || Collection.class.isAssignableFrom(o.getClass()))
                .map(o -> (Collection<?>) (o.getClass().isArray() ? Arrays.asList(((Object[]) o)) : o))
                .ifPresent(collection -> {
                    collection.forEach(c -> {
                        Optional.ofNullable(TypeUtils.of(c.getClass()))
                                .ifPresent(t -> {
                                    Type element = getElement();
                                    if (element == null || t.isType(element)) {
                                        entity.add(t.toEntity(c));
                                    }
                                });

                    });
                });

        return entity;
    }
}
