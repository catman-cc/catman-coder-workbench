package cc.catman.coder.workbench.core.entity;

import cc.catman.coder.workbench.core.type.raw.BooleanRawType;

public class BooleanEntity extends RawEntity<Boolean> {
    public BooleanEntity() {
        super(new BooleanRawType());
    }

    public BooleanEntity( Boolean value) {
        super(new BooleanRawType(),value);
    }
}
