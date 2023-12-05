package cc.catman.coder.workbench.core.apis.configurations.result;

import java.lang.annotation.*;

/**
 * 统一返回结果切面将会忽略掉标注了该注解的方法或者类
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreResult {
}
