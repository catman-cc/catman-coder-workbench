package cc.catman.coder.workbench.core.entity;

import cc.catman.coder.workbench.core.type.raw.StringRawType;


public class StringEntity extends RawEntity<String> {

    public StringEntity() {
        super(new StringRawType());
    }
    public StringEntity( String value) {
        super(new StringRawType(), value);
    }
}
