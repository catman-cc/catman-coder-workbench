package cc.catman.coder.workbench.core.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.catman.coder.workbench.core.entity.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class DefaultType implements Type {

    @Setter
    protected String typeName;

    /**
     * 值得注意的是,items并不一定包含了全量的子类型定义,这里只包含了非公开的子类型定义
     * 全量子类型定义需要通过sortedItems来获取
     */
    @Getter
    @Setter
    @Builder.Default
    protected Map<String,TypeDefinition> privateItems = new HashMap<>();

    @Getter
    @Setter
    @Builder.Default
    protected List<TypeItem> sortedAllItems = new ArrayList<>();

    public DefaultType add(TypeDefinition typeDefinition) {
        privateItems.put(typeDefinition.getId(),typeDefinition);
        return this;
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return false;
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
