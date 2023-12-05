package cc.catman.coder.workbench.core.entity;

import cc.catman.coder.workbench.core.type.Type;

public interface Entity<T> {
    Type getType();
   T toObject();
   default T getValue(){
       return toObject();
   }
}
