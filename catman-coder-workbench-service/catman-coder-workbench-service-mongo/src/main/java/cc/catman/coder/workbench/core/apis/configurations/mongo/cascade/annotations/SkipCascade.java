package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于跳过级联处理,当方法上标注了{@link SkipCascade}后,涉及到的操作将不会执行级联
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipCascade {
}
