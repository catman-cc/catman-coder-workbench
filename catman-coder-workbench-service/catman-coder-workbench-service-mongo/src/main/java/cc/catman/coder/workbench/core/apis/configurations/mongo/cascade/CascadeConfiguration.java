package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade;

import cc.catman.coder.workbench.core.apis.configurations.common.AnnotationAspectPointCutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.SkipCascade;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.skip.SkipCascadeMethodInterceptor;

// @Configurable
@Configuration
@Import(CascadeMongoEventListener.class)
public class CascadeConfiguration {

    @Bean
    public AnnotationAspectPointCutAdvisor annotationAspectPointCutAdvisor() {
        return new AnnotationAspectPointCutAdvisor(SkipCascade.class, new SkipCascadeMethodInterceptor());
    }
}
