package cc.catman.workbench.configuration.snapshot;

import java.lang.annotation.*;

/**
 * 标注了该注解的方法,在发生变化时,将自动创建快照数据
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UseSnapshot {
    /**
     * 用于提供资源的kind
     */
    String value();


}
