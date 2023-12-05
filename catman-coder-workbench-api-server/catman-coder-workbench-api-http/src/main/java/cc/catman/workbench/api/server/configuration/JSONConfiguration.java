package cc.catman.workbench.api.server.configuration;

import cc.catman.coder.workbench.core.JSONMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONConfiguration {
    @Bean
    public JSONMapper jsonMapper(ObjectMapper objectMapper){
        return new JSONMapper(){
            @Override
            @SneakyThrows
            public <T> T fromJson(String json, Class<T> clazz) {
                return objectMapper.readValue(json,clazz);
            }

            @Override
            @SneakyThrows
            public String toJson(Object object) {
                return objectMapper.writeValueAsString(object);
            }
        };
    }
}
