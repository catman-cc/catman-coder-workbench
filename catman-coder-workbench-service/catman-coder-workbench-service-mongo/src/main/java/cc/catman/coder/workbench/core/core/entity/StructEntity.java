package cc.catman.coder.workbench.core.core.entity;

import cc.catman.coder.workbench.core.core.type.complex.StructType;
import cc.catman.coder.workbench.core.core.type.Type;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.core.type.TypeRuntimeException;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class StructEntity implements Entity<Map<String,Object>>{

    @Getter
    private StructType type;

    private Map<String,Entity<?>> entities=new HashMap<>();

    public StructEntity(StructType type) {
        this.type = type;
        type.getItems().forEach(d->{
            entities.put(d.getName(),null);
        });
    }

    public StructEntity set(String name,Object value){
        if (!entities.containsKey(name)){
            throw new TypeRuntimeException("");
        }

        TypeDefinition typeDefinition = Optional.ofNullable(type.definitionMap.get(name))
                .orElseThrow(() -> new TypeRuntimeException(String.format("type definition [%s] not found in [%s]",name,type.toString())));

        Type t = typeDefinition.getType();
        if (Optional.ofNullable(value).isEmpty()){
            this.entities.put(name,t.toEntity(null));
            return this;
        }
        if (value instanceof Entity<?> entity) {
            if (entity.getType().canConvert(t)){
                this.entities.put(name,entity);
            }else {
                this.entities.put(name,t.toEntity(entity.toObject()));
            }
        }else {
            Entity<?> entity = t.toEntity(value);
            this.entities.put(name,entity);
        }

        return this;
    }

    @Override
    public Map<String,Object> toObject(){
        Map<String,Object> obj=new LinkedHashMap<>();
        type.getItems().forEach(td->{
            Entity<?> entity = this.entities.get(td.getName());
            obj.put(td.getName(),Optional.ofNullable(entity).map(Entity::toObject).orElse(null));
        });
        return obj;
    }
}
