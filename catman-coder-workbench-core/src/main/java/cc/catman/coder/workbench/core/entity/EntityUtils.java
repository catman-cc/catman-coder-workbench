package cc.catman.coder.workbench.core.entity;

import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.MapType;
import cc.catman.coder.workbench.core.type.complex.StructType;

import java.util.Collection;
import java.util.Map;

public class EntityUtils {

    Entity<?> wrapper(Object o){
        if (o instanceof Map<?,?> map){
            return MapType.builder()

                    .build().toEntity(o);
        }else if (o instanceof Collection<?> c){
            return ArrayType.builder()
                    .build()
                    .toEntity(o);
        }

        else {
           return StructType.builder()
                    .className(o.getClass().getCanonicalName())
                    .build().toEntity(o);
        }
    }
}
