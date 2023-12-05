package cc.catman.coder.workbench.core.core;

import cc.catman.coder.workbench.core.core.type.TypeDefinition;

public interface Constants {
    interface Type{
        String TYPE_NAME_STRING="string";
        String TYPE_NAME_NUMBER = "number";
        String TYPE_NAME_STRUCT = "struct";
        String TYPE_NAME_ARRAY = "array";
        String TYPE_NAME_BOOLEAN = "boolean";
        String TYPE_NAME_MAP = "map";
        String TYPE_NAME_SLOT="slot";
        String TYPE_NAME_REFER="refer";
    }
    interface DefaultValue{
        ValueCreator<Boolean> BOOLEAN=()->false;

        ValueCreator<String>  STRING=()->"";
        ValueCreator<Number>  NUMBER=()-> 0;
    }

    interface  Label{
       String PREFIX="cc.catman.plugin.core";
        /**
         * 用于标记类名
         */
       String CLASS_NAME=PREFIX+"/"+"className";
    }

    interface ResourceKind{
        String TYPE_DEFINITION= TypeDefinition.class.getName();
    }
}
