package cc.catman.coder.workbench.core.core.entity;

import cc.catman.coder.workbench.core.core.type.raw.BooleanRawType;

public class BooleanEntity extends RawEntity<Boolean> {
    public BooleanEntity() {
        super(new BooleanRawType());
    }

    public BooleanEntity( Boolean value) {
        super(new BooleanRawType(),value);
    }
}
