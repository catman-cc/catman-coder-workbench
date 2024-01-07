package cc.catman.coder.workbench.core.type;

import lombok.Getter;

@Getter
public enum ETypeName {
    ANY("any", true, true, true, true, true, true, true, true, true),
    GENERIC("generic", false, true, true, true, true, true, true, true, true),
    BOOLEAN("boolean", true, false, false, false, false, false, false, false, false),
    FILE("file", true, false, false, false, false, false, false, false, false),
    NUMBER("number", true, false, false, false, false, false, false, false, false),
    STRING("string", true, false, false, false, false, false, false, false, false),
    ANONYMOUS("anonymous", false, false, false, false, false, false, false, false, false),
    STRUCT("struct", false, false, false, false, false, false, false, false, false),
    ARRAY("array", false, false, false, false, false, false, false, false, false),
    MAP("map", false, false, false, false, false, false, false, false, false),
    SLOT("slot", false, false, false, false, false, false, false, false, false),
    REFER("refer", false, false, false, false, false, false, false, false, false),
    ;
    private String typeName;
    private boolean raw;
    private boolean array;
    private boolean complex;
    private boolean struct;
    private boolean refer;
    private boolean map;
    private boolean slot;
    private boolean any;
    private boolean generic;

    ETypeName(String typeName, boolean raw, boolean array, boolean complex, boolean struct, boolean refer, boolean map, boolean slot, boolean any, boolean generic) {
        this.typeName = typeName;
        this.raw = raw;
        this.array = array;
        this.complex = complex;
        this.struct = struct;
        this.refer = refer;
        this.map = map;
        this.slot = slot;
        this.any = any;
        this.generic = generic;
    }
    public static ETypeName from(String typeName){
        for (ETypeName value : ETypeName.values()) {
            if(value.typeName.equals(typeName)){
                return value;
            }
        }
        return null;
    }
}
