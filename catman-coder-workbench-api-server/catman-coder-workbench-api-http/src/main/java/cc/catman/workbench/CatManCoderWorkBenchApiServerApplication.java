package cc.catman.workbench;

import cc.catman.workbench.api.server.CatmanWorkbenchDeveloperModProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
@EnableConfigurationProperties(CatmanWorkbenchDeveloperModProperties.class)
public class CatManCoderWorkBenchApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatManCoderWorkBenchApiServerApplication.class, args);
    }
}
