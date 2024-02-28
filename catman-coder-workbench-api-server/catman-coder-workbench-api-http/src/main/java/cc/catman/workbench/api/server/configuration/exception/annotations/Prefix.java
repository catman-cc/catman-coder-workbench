package cc.catman.workbench.api.server.configuration.exception.annotations;

import java.lang.annotation.*;

/**
 * 异常码统一前缀，默认使用异常码枚举类名称去除E?Code后缀，比如：GlobalECode对应Global
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Prefix {
    /**
     * 异常码统一前缀
     */
    String value() default "";

    /**
     * 异常码连接符
     */
    String split() default "-";
}
