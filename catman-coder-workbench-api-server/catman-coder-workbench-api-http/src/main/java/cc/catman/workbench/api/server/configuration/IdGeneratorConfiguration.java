package cc.catman.workbench.api.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

@Configuration
public class IdGeneratorConfiguration {
    @Bean
    public IdGenerator idGenerator(){
        return new AlternativeJdkIdGenerator();
    }
}
