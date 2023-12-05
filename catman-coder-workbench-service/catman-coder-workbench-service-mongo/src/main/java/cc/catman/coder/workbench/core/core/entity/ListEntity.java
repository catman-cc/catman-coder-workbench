package cc.catman.coder.workbench.core.core.entity;

import java.util.ArrayList;
import java.util.List;

import cc.catman.coder.workbench.core.core.type.DefaultType;
import cc.catman.coder.workbench.core.core.type.complex.ArrayType;
import lombok.Getter;

public class ListEntity implements Entity<List<Object>> {

    @Getter
    private ArrayType type;

    public ListEntity(DefaultType e) {
        this.type = new ArrayType();
        this.type.setElement(e);
    }

    private List<Entity<?>> items = new ArrayList<>();

    public ListEntity add(Entity<?> entity) {
        this.items.add(entity);
        return this;
    }

    @Override
    public List<Object> toObject() {
        return new ArrayList<>(items.stream().map(Entity::toObject).toList());
    }

    public Object get(int index) {
        return items.size() < index ? null : items.get(index).toObject();
    }
}
