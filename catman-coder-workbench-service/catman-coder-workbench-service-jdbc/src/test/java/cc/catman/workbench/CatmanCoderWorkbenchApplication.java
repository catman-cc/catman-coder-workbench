package cc.catman.workbench;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "cc.catman.workbench.dao.core.repossitory")
@SpringBootApplication
public class CatmanCoderWorkbenchApplication extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatmanCoderWorkbenchApplication.class, args);
    }
}
