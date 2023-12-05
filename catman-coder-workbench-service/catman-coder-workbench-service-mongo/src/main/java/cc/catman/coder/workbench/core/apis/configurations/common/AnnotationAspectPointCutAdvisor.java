package cc.catman.coder.workbench.core.apis.configurations.common;


import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 对自定义注解进行切面处理的自定义类
 *
 * @author Hanqi <jpanda@aliyun.com>
 * @since 2018/12/10 14:43
 */
public class AnnotationAspectPointCutAdvisor extends AbstractPointcutAdvisor {

    /**
     * 需要处理的切面注解
     */
    private final Class<? extends Annotation> targetAnnotation ;

    /**
     * 方法拦截处理器
     */
    private final MethodInterceptor interceptor;

    /**
     * 切入点
     */
    private final StaticMethodMatcherPointcut pointcut = new AnnotationAspectPoint();

    public AnnotationAspectPointCutAdvisor(Class<? extends Annotation> targetAnnotation, MethodInterceptor interceptor) {
        this.targetAnnotation = targetAnnotation;
        this.interceptor = interceptor;
    }
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }


    @Override
    public Advice getAdvice() {
        return interceptor;
    }

    private final class AnnotationAspectPoint extends StaticMethodMatcherPointcut {

        @Override
        public boolean matches(Method method, Class<?> targetClass) {

            assert targetAnnotation != null;

            return AnnotationUtils.findAnnotation(method, targetAnnotation) != null
                   || AnnotationUtils.findAnnotation(targetClass, targetAnnotation) != null;
        }
    }
}