package cc.catman.coder.workbench.core.entity;

import cc.catman.coder.workbench.core.type.raw.NumberRawType;

public class NumberEntity extends RawEntity<Number>{
    public NumberEntity() {
        super(new NumberRawType());
    }

    public NumberEntity( Number value) {
        super(new NumberRawType(), value);
    }
}
