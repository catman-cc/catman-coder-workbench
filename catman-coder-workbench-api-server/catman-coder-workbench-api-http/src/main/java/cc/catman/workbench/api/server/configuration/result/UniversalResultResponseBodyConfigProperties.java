package cc.catman.workbench.api.server.configuration.result;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "jpanda.auto-result")
public class UniversalResultResponseBodyConfigProperties {

    private boolean processByDefault=true;
    /**
     * 自动处理返回结果的包
     */
    private List<String> includePackages;
    /**
     * 禁止自动处理返回结果的包，优先级高于includePackages
     */
    private List<String> excludesPackages;
}
