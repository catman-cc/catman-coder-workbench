package cc.catman.coder.workbench.core.type;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Optional;

@Data
@Builder
public class TypeDefinitionAnalyzer {
    private Object object;
    private String name;

    public TypeDefinition analyze() {
        if (object == null) {
            return null;
        }
        if(object instanceof Class<?>){
            return analyzeClass((Class<?>) object);
        }
        if (object instanceof DefaultType type){
            return TypeDefinition.builder().name(name).type(type).build();
        }
        if (object instanceof TypeDefinition typeDefinition){
            return typeDefinition;
        }
        return analyzeClass(object.getClass());
    }

    private TypeDefinition analyzeClass(Class<?> object) {
        TypeAnalyzer typeAnalyzer = TypeAnalyzer.builder().typeObject(object).build();
        // 获取到类型
        DefaultType analyzer = typeAnalyzer.analyzer();
        // 我觉得这里应该读取注解
        return processAnnotation(TypeDefinition.builder().name(name).type(analyzer).build(),object);
    }

    public TypeDefinition processAnnotation(TypeDefinition typeDefinition,Class<?> clazz){
        // 获取到注解
       return Optional.ofNullable(AnnotationUtils.getAnnotation(clazz, TD.class)).map(a->{
            typeDefinition.setName(a.name());
            typeDefinition.setDescribe(a.desc());
            typeDefinition.setRequired(a.required());
            return typeDefinition;
        }).orElse(typeDefinition);
    }

}
