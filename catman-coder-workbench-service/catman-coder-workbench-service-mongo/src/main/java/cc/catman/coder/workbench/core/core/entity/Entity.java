package cc.catman.coder.workbench.core.core.entity;

import cc.catman.coder.workbench.core.core.type.Type;

public interface Entity<T> {
    Type getType();
   T toObject();
   default T getValue(){
       return toObject();
   }
}
