package cc.catman.workbench.api.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "catman.workbench.developer")
public class CatmanWorkbenchDeveloperModProperties {
    /**
     * 是否启用开发者模式
     */
    private boolean enable;
    /**
     * 运行模式
     */
    private EDeveloperMod mod = EDeveloperMod.DEVELOPER;
}
