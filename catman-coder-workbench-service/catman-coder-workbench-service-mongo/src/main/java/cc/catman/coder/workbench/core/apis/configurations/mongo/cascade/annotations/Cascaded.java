package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cascaded {
    CascadeItem[] value() default {};
}
