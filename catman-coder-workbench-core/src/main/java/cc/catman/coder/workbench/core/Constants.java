package cc.catman.coder.workbench.core;

import cc.catman.coder.workbench.core.type.TypeDefinition;

public interface Constants {
    String TEMP_ID_SUFFIX="@TMP-";
    interface Type{
        String TYPE_NAME_STRING="string";
        String TYPE_NAME_NUMBER = "number";
        String TYPE_NAME_STRUCT = "struct";
        String TYPE_NAME_ARRAY = "array";
        String TYPE_NAME_BOOLEAN = "boolean";
        String TYPE_NAME_MAP = "map";
        String TYPE_NAME_SLOT="slot";
        String TYPE_NAME_REFER="refer";
        String TYPE_NAME_ENUM="enum";
        String TYPE_NAME_GENERIC="generic";
        String TYPE_NAME_ANY="any";
        String TYPE_NAME_FILE="file";


        String TYPE_NAME_ANONYMOUS="anonymous";

        String TYPE_NAME_FUNCTION="function";
    }

    interface ValueProviderKind{
        String IF="if";
        String SWITCH="switch";
        String EXPRESSIONS="expressions";
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

        /**
         * 用于标记资源所属的插件,这样可以对资源进行插件化的扩展和修改
         */
       String BELONG_PLUGIN=PREFIX+"/"+"belong-plugin";

    }

    interface ResourceKind{
        String TYPE_DEFINITION= TypeDefinition.class.getName();
    }
}
