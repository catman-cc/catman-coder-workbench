package cc.catman.workbench.api.server.configuration.result;

import java.lang.annotation.*;

/**
 * 在返回对象已经是{@link Result}接口的实现类时，是否依然进行包装
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ForceResult {
}
