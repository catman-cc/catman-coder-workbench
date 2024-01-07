package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeAnalyzer;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class TypeToParameterAnalyzer {

    private Object object;

    private String name;

    private String forceType;

    public Parameter analyzer(){
        if (object==null){
            return null;
        }
        if (object instanceof TypeDefinition td){
            return analyzer(td,true);
        }
        if (object instanceof DefaultType t){
            if (StringUtils.hasLength(forceType)){
                t.setTypeName(forceType);
            }
            return analyzer(TypeDefinition.builder()
                    .id(idGenerator())
                    .name(t.getTypeName())
                    .type(t)
                    .build(),true);
        }
        if (object instanceof Class<?> t){
            DefaultType dt = TypeAnalyzer.builder().typeObject(t)
                    .forceType(forceType)
                    .build().analyzer();
            return analyzer(TypeDefinition.builder()
                    .id(idGenerator())
                    .name(dt.getTypeName())
                    .type(dt)
                    .build(),true);
        }
        DefaultType dt = TypeAnalyzer.builder().typeObject(object.getClass()).forceType(forceType).build().analyzer();
        return analyzer(TypeDefinition.builder()
                .id(idGenerator())
                .name(dt.getTypeName())
                .type(dt)
                .build(),true);
    }

    Parameter analyzer(TypeDefinition type,boolean isRoot){
       return Parameter.builder()
                .id(idGenerator())
                .name(Optional.ofNullable(name).filter((v)->isRoot).orElse(type.getName()))
                .type(type)
                .value(isRoot? ValueProviderDefinition.builder()
                        .id(idGenerator())
                        .kind("sameName")
                        .args(Parameter.builder()
                                .id(idGenerator())
                                .name("name")
                                .type(TypeDefinition.builder()
                                        .id(idGenerator())
                                        .name("name")
                                        .type(DefaultType.builder().typeName("string").build())
                                        .build())
                                .build())
                        .build():null)
                .items(
                        type.getType().isComplex()
                                ?type.getType().getPrivateItems().values()
                                .stream().map(item-> analyzer(item,false))
                                .collect(Collectors.toList())
                                : new ArrayList<>()
                ).build();
    }
    protected String idGenerator(){
        return Constants.TEMP_ID_SUFFIX + UUID.randomUUID().toString();
    }
}
