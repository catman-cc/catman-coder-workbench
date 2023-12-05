package cc.catman.coder.workbench.core.type.complex;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.entity.Entity;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.Type;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.TypeRuntimeException;
import cc.catman.coder.workbench.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MapType extends ComplexType {

    @Builder.Default
    public transient Map<String,TypeDefinition> definitionMap=new HashMap<>();

    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_MAP;
    }

    public MapType add(String name, DefaultType type){
        return add(TypeDefinition.builder().name(name).type(type).build());
    }

    public MapType add(TypeDefinition typeDefinition){
        Optional.ofNullable(typeDefinition.getName())
                .orElseThrow(()-> new TypeRuntimeException("The @name field of Type Definition must not be empty"));

        if (definitionMap.containsKey(typeDefinition.getName())){
            throw new TypeRuntimeException("Duplicate elements are created for the struct: @"+typeDefinition.getName());
        }
        this.definitionMap.put(typeDefinition.getName(),typeDefinition);
        this.items.add(typeDefinition);
        return this;
    }

    public MapType addString(String name){
        return add(TypeDefinition.builder()
                .name(name)
                .type(new StringRawType())
                .build());
    }

    public MapType addBoolean(String name){
        return add(TypeDefinition.builder()
                .name(name)
                .type(new BooleanRawType())
                .build());
    }

    public MapType addNumber(String name){
        return add(TypeDefinition.builder()
                .name(name)
                .type(new NumberRawType())
                .build());
    }

    @Override
    public boolean canConvert(Type target){
        if (!target.isMap()){
            return false;
        }
        // 然后依次判断,target中所需的结构当前结构是否[全部]包含
        if (target instanceof MapType tt){
            List<TypeDefinition> tvs = tt.getItems();
            Map<String,TypeDefinition> cvs=this.getItems().stream().collect(Collectors.toMap(TypeDefinition::getName, n->n));
            return tvs.stream()
                    .allMatch(tv-> Optional.ofNullable(cvs.get(tv.getName()))
                            .map(cv-> cv.getType().canConvert(tv.getType()))
                            .orElse(false));
        }
        // never exec ...
        return false;
    }

    @Override
    public boolean isType(Type target) {
        if (!target.isMap()){
            return false;
        }
        // 然后依次判断,target中所需的结构当前结构是否[全部]包含
        if (target instanceof MapType tt){
            List<TypeDefinition> tvs = tt.getItems();
            Map<String,TypeDefinition> cvs=this.getItems().stream().collect(Collectors.toMap(TypeDefinition::getName, n->n));
            return tvs.stream()
                    .allMatch(tv-> Optional.ofNullable(cvs.get(tv.getName()))
                            .map(cv-> cv.getType().isType(tv.getType()))
                            .orElse(false));
        }
        // never exec ...
        return false;
    }

    @Override
    public Entity<Map<String,Object>> toEntity(Object o) {
        return null;
    }

    @Override
    public boolean isStruct() {
        return true;
    }
}
