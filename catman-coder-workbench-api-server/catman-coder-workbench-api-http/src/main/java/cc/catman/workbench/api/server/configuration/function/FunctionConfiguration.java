package cc.catman.workbench.api.server.configuration.function;

import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.workbench.service.core.services.DefaultFunctionInfoManager;
import cc.catman.workbench.service.core.services.IFunctionInfoManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FunctionConfiguration {
    @Bean
    public IFunctionInfoManager functionInfoManager() {
        DefaultFunctionInfoManager defaultFunctionInfoManager = new DefaultFunctionInfoManager();
        for (FunctionInfo info : new FunctionInfoProvider().provider()) {
            defaultFunctionInfoManager.addInnerFunctionInfo(info.getKind(), info);
        }
        return defaultFunctionInfoManager;
    }
}
