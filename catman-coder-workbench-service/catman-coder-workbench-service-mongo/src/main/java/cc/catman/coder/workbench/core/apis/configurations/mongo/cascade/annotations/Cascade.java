package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core.CascadeFillType;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core.CascadeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cascade {

    CascadeType value() default CascadeType.SAVE;

    /**
     * ----------------------------------------------------------------------------
     *      EL表达式,可以访问到以下内容:
     *        - root: 获取到字段所属的对象信息
     *        - value: 在运行时字段对应的值
     *        - cascade: 获取到当前注解实例
     *        - supportType: 获取所支持的CascadeType
     *        - each: 当字段是一个集合时,通过#each将依次访问集合中的每一个元素
     *        - field: 访问到字段本身
     *        - type: 获取到字段的类型
     *----------------------------------------------------------------------------
     * when表达式: 必须返回boolean值,在运行时,将会依次评估所有表达式,
     *      返回true,表示数据匹配,将进行级联操作
     */
    String[] when()default {};

    /**
     * 控制回填数据的方式
     */
    CascadeFillType fillType() default CascadeFillType.ALL;
}