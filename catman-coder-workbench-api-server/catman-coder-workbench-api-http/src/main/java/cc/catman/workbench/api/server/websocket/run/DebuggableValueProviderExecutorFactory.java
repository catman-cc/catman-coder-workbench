package cc.catman.workbench.api.server.websocket.run;

import cc.catman.coder.workbench.core.value.ValueProviderExecutor;
import cc.catman.coder.workbench.core.value.ValueProviderExecutorFactory;
import cc.catman.workbench.api.server.websocket.run.value.DebuggableValueProviderExecutor;

public class DebuggableValueProviderExecutorFactory implements ValueProviderExecutorFactory {
    public ValueProviderExecutor create() {
        new DebuggableValueProviderExecutor();
        return null;
    }
}
