package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.skip;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SkipCascadeMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        SkipThreadLocal.set(true);
        Object proceed = invocation.proceed();
        SkipThreadLocal.clear();
        return proceed;
    }
}
