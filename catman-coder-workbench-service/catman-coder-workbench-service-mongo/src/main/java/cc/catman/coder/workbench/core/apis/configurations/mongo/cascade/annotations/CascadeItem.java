package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CascadeItem {
    /**
     *  在删除时,执行该语句获取引用该Collection的数量,支持EL表达式
     *  EL上下文参考: {@link Cascade#when()}
     */
    String query();

    /**
     * 在删除时,执行该语句,获取的记录将会被排除到统计的范围之外,支持EL表达式
     * EL上下文参考: {@link Cascade#when()}
     */
    String excludeQuery() default "";

    /**
     * 如果query - excludeQuery <=minCount ,将会触发删除操作
     */
    long minCount() default 0;

    String collection() default "";

    Class<?> entity() default Object.class;
}
