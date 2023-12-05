package cc.catman.coder.workbench.core.type.raw;

import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.Type;
import lombok.Getter;

public abstract class RawType<T> extends DefaultType {


    @Getter
    protected T defaultValue;

    public RawType(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isRaw() {
        return true;
    }

    public boolean isType(Type target){
        return target.isRaw()&&target.getTypeName().equals(getTypeName());
    }

}