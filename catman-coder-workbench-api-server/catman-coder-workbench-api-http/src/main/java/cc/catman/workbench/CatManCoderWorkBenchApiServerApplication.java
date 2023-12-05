package cc.catman.workbench;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories()
public class CatManCoderWorkBenchApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatManCoderWorkBenchApiServerApplication.class, args);
    }
}
