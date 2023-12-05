package cc.catman.workbench.configuration.snapshot;

import cc.catman.workbench.service.core.services.ISnapshotService;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class UseSnapshotMethodInterceptor implements MethodInterceptor {
    private ISnapshotService snapshotService;

    public UseSnapshotMethodInterceptor(ISnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //
        return null;
    }
}
