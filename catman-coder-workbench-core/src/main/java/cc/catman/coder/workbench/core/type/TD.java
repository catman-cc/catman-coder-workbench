package cc.catman.coder.workbench.core.type;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 类型定义
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface TD {

    /**
     * 类型的名称
     */
    String name() default "";

    /**
     * 类型的描述
     */
    String desc() default "";


    /**
     * 是否必须
     */
    boolean required() default false;
}
