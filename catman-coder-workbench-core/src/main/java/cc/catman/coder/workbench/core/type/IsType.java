package cc.catman.coder.workbench.core.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IsType {
    String value()default "";
}
