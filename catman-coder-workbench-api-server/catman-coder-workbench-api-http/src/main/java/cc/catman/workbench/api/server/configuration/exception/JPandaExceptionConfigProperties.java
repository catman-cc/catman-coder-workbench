package cc.catman.workbench.api.server.configuration.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "catman.exception.handler")
public class JPandaExceptionConfigProperties {
    private boolean returnErrorInfo;
}
