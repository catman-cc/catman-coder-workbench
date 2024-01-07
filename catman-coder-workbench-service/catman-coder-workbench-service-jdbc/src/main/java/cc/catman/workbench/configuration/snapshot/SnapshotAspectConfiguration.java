package cc.catman.workbench.configuration.snapshot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class SnapshotAspectConfiguration {
    @Bean
    public  AnnotationAspectPointCutAdvisor annotationAspectPointCutAdvisor(){
        return new AnnotationAspectPointCutAdvisor();
    }
}
