package cc.catman.workbench.api.server.configuration.function;

import cc.catman.workbench.service.core.services.DefaultFunctionInfoManager;
import cc.catman.workbench.service.core.services.IFunctionInfoManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FunctionConfiguration {
    @Bean
    public IFunctionInfoManager functionInfoManager() {
        return new DefaultFunctionInfoManager();
    }
}
