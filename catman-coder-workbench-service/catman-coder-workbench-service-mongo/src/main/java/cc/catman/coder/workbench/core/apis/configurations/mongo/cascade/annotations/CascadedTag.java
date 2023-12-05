package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果一个对象在设计上就是作为嵌套文档存在的,但是内部元素又想使用Cascade特性,可以使用该标签
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CascadedTag {
}
