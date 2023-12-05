package cc.catman.coder.workbench.core.core.type.complex;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import cc.catman.coder.workbench.core.core.Constants;
import cc.catman.coder.workbench.core.core.entity.StructEntity;
import cc.catman.coder.workbench.core.core.type.*;
import cc.catman.coder.workbench.core.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.core.type.raw.StringRawType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Transient;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * StructType是一种特殊的map类型,它是由java class转换而来
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class StructType extends MapType{
    /**
     * class的名称
     */
    private String className;

    @Cascade
    protected StructType parent;

    @JsonIgnore
    @Transient
    @Builder.Default
    public transient Map<String, TypeDefinition> definitionMap=new HashMap<>();

    public StructType(String className) {
        this.className = className;
    }
        public Map<String,TypeDefinition> getDefinitionMap(){
        if (definitionMap.size()!=0||items.size()==0){
            return definitionMap;
        }

        HashMap<String,TypeDefinition> dm = new HashMap<>();

        this.items.forEach(typeDefinition->{
            definitionMap.put(typeDefinition.getName(),typeDefinition);
        });
    return this.definitionMap;
    }
    @Override
    public String getTypeName() {
        return Constants.Type.TYPE_NAME_STRUCT;
    }

    public StructType add(String name, DefaultType type){
        return add(TypeDefinition.builder().name(name).type(type).build());
    }

    public StructType add(TypeDefinition typeDefinition){
        Optional.ofNullable(typeDefinition.getName())
                .orElseThrow(()-> new TypeRuntimeException("The @name field of Type Definition must not be empty"));

        if (getDefinitionMap().containsKey(typeDefinition.getName())){
            throw new TypeRuntimeException("Duplicate elements are created for the struct: @"+typeDefinition.getName());
        }
        this.getDefinitionMap().put(typeDefinition.getName(),typeDefinition);
        this.items.add(typeDefinition);
        return this;
    }

    public StructType addString(String name){
        return add(TypeDefinition.builder()
                .name(name)
                .type(new StringRawType())
                .build());
    }

    public StructType addBoolean(String name){
        return add(TypeDefinition.builder()
                .name(name)
                .type(new BooleanRawType())
                .build());
    }

    public StructType addNumber(String name){
        return add(TypeDefinition.builder()
                .name(name)
                .type(new NumberRawType())
                .build());
    }

    @Override
    public boolean canConvert(Type target){
        if (!target.isStruct()){
            return false;
        }
        // 然后依次判断,target中所需的结构当前结构是否[全部]包含
        if (target instanceof StructType tt){
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
        if (!target.isStruct()){
            return false;
        }

        // 然后依次判断,target中所需的结构当前结构是否[全部]包含
        if (target instanceof StructType tt){
            if (getClassName().equals( tt.getClassName())){
                return true;
            }
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
    protected  List<TypeDefinition> allTypeDefinitions(){
        List<TypeDefinition> ptds = new ArrayList<>(Optional.ofNullable(parent)
                .map(StructType::allTypeDefinitions).orElse(Collections.emptyList()));
        this.items.forEach(item->{
            Optional<TypeDefinition> first = ptds.stream().filter(ptd -> ptd.getName().equals(item.getName())).findFirst();
            if (first.isEmpty()){
                ptds.add(item);
            }else {
                TypeDefinition typeDefinition = first.get();
                int i = ptds.indexOf(typeDefinition);
                ptds.remove(typeDefinition);
                ptds.add(i,item);
            }
        });
        return ptds;
    }
    @Override
    public StructEntity toEntity(Object obj) {
        List<TypeDefinition> items = allTypeDefinitions();
        StructEntity se=new StructEntity(this);
        Optional.ofNullable(obj).ifPresent(o->{
            // 此时可以处理值的只有map和用户自定义对象
            if (o instanceof Map<?,?> map){
                items.forEach(i->{
                    i.getType().toEntity(map.get(i.getName()));
                });
            }else if (AnnotationUtils.isAnnotationDeclaredLocally(IsType.class,o.getClass())){
                items.forEach(i->{
                    Optional.ofNullable(ReflectionUtils.findField(o.getClass(), i.getName()))
                            .map(field -> {
                                ReflectionUtils.makeAccessible(field);
                                return field;
                            })
                            .ifPresent(field->{
                                Object v = null;
                                try {
                                    v = field.get(o);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                                se.set(i.getName(),v);
                            });
                });
            }
        });

        return se;
    }

    @Override
    public boolean isStruct() {
        return true;
    }
}
