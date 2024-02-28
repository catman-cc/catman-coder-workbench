package cc.catman.workbench.api.server.configuration.fury;

import cc.catman.coder.workbench.core.serialization.FurySerialization;
import cc.catman.coder.workbench.core.serialization.Hessian2Serialization;
import cc.catman.coder.workbench.core.serialization.ICatManSerialization;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import org.apache.fury.Fury;
import org.apache.fury.config.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializationConfiguration {
    @Bean
    public ICatManSerialization serialization(){
//       Fury fury=Fury.builder()
//               .withLanguage(Language.JAVA)
//               .withRefTracking(true)
//               .requireClassRegistration(false)
//               .build();
//       fury.register(TypeDefinition.class);
//       return new FurySerialization(fury);
        return new Hessian2Serialization();
    }
}
