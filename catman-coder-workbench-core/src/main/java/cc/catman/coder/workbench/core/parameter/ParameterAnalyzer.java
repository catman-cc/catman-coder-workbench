package cc.catman.coder.workbench.core.parameter;


import cc.catman.plugin.classloader.handler.RedirectClassLoaderHandler;
import cc.catman.plugin.classloader.matcher.DefaultClassNameMatcher;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class ParameterAnalyzer {

    static DefaultClassNameMatcher classNameMatcher = DefaultClassNameMatcher
            .of(RedirectClassLoaderHandler.JDK_EXCLUDED_PACKAGES);

    public Parameter analyze(Class<?> clazz) {
        // 判断类型定义是否可以被参数化
        // 如果可以,则进行参数化,否则返回null

        return null;
    }

    public Parameter analyze(Class<?> clazz, String name) {
        return null;
    }

    public Parameter analyze(Field field){
        return null;
    }

    protected boolean isParameterizable(Class<?> clazz){
        //  判断类型定义是否可以被参数化,自定义对象可以被参数化
        //  jdk自带基本数据类型和集合可以被参数化
        //  其他类型不可以被参数化
        // 1. 判断是否是jdk自带的基本数据类型,根据包名判断
        // 2. 判断是否是jdk自带的集合类型
        // 3. 判断是否是自定义对象
        // 4. 判断是否是jdk自带的其他类型


        return false;
    }
}
