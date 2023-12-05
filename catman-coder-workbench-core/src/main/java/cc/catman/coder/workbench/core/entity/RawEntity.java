package cc.catman.coder.workbench.core.entity;

import cc.catman.coder.workbench.core.type.raw.RawType;
import lombok.Getter;

import java.util.Optional;

public class RawEntity<T> implements Entity<T> {

    @Getter
    protected RawType<T> type;

    protected T value;

    public RawEntity(RawType<T> type) {
        this.type = type;
    }

    public RawEntity(RawType<T> type, T value) {
        this.type = type;
        this.value = value;
    }

    public T getValue(){
        return Optional.ofNullable(value).orElse(type.getDefaultValue());
    }

    @Override
    public T toObject() {
        return value;
    }
}
