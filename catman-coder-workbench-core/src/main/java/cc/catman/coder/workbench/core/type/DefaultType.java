package cc.catman.coder.workbench.core.type;

import java.util.ArrayList;
import java.util.List;

import cc.catman.coder.workbench.core.entity.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class DefaultType implements Type {

    protected String typeName;

    @Getter
    @Setter
    @Builder.Default
    protected List<TypeDefinition> items = new ArrayList<>();

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
