package cc.catman.coder.workbench.core.type;

import lombok.Builder;
import lombok.Data;

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
        DefaultType analyzer = typeAnalyzer.analyzer();
        return TypeDefinition.builder().name(name).type(analyzer).build();
    }

}
