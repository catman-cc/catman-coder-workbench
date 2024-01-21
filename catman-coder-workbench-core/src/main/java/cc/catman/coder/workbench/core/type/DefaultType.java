package cc.catman.coder.workbench.core.type;

import java.util.*;
import java.util.function.Function;

import cc.catman.coder.workbench.core.DefaultLoopReferenceContext;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.entity.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
@NoArgsConstructor
public class DefaultType implements Type {

    @Setter
    protected String typeName;

    /**
     * 值得注意的是,items并不一定包含了全量的子类型定义,这里只包含了非公开的子类型定义
     * 全量子类型定义需要通过sortedItems来获取
     */
    @Getter
    @Setter
    @Builder.Default
    protected Map<String,TypeDefinition> privateItems = new HashMap<>();

    @Getter
    @Setter
    @JsonIgnore
    @Builder.Default
    private transient Map<String,TypeDefinition> publicItems = new HashMap<>();

    @Getter
    @Setter
    @Builder.Default
    protected List<TypeItem> sortedAllItems = new ArrayList<>();

    @Getter
    @Setter
    @Builder.Default
    protected transient ILoopReferenceContext context= DefaultLoopReferenceContext.create();

    public DefaultType add(TypeDefinition typeDefinition) {
        privateItems.put(typeDefinition.getId(),typeDefinition);
        context.add(typeDefinition);
        return this;
    }

    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public boolean canConvert(Type targetType) {
        return false;
    }

    @Override
    public boolean isType(Type target) {
        return false;
    }

    @Override
    public Entity<?> toEntity(Object o) {
        return null;
    }

    public void synchronize(){
        this.populatePublicTypeDefinition(new HashMap<>());
    }
    public  void synchronize(Map<String,TypeDefinition> typeDefinitions){
        this.populatePublicTypeDefinition(typeDefinitions);
    }
    public void populatePublicTypeDefinition(Map<String,TypeDefinition> publicTypeDefinitions) {
        if (this.publicItems.size()>0){
            this.publicItems.forEach((key,value)->{
                if (publicTypeDefinitions.containsKey(key)){
                    TypeDefinition typeDefinition = publicTypeDefinitions.get(key);
                    if (!typeDefinition.equals(value)){
                        log.warn("typeDefinition is not equals, key:{}, value:{}, typeDefinition:{}",key,value,typeDefinition);
                    }
                }else {
                    publicTypeDefinitions.put(key,value);
                }
            });
        }
        this.publicItems = publicTypeDefinitions;
        // 此处只同步了私有类型,没有同步公开类型,这就意味着public的定义中的publicItems可能是不完整的
        this.privateItems.values()
                .forEach(i -> i.populatePublicTypeDefinition(publicTypeDefinitions));

        // 同步公开类型,这样就可以保证publicItems是完整的
        this.publicItems.values()
                .forEach(i -> {
                    if(i.getType().publicItems.equals(this.publicItems)){
                        return;
                    }
                    i.populatePublicTypeDefinition(publicTypeDefinitions);
                });

    }
    public TypeDefinition getItem(String id) {
       return Optional.ofNullable( this.privateItems.get(id))
               .orElseGet(()-> this.publicItems.get(id));
    }

    public TypeDefinition getItem(TypeItem typeItem) {
        return this.getItem(typeItem.getItemId());
    }

    public TypeDefinition getMust(String id) {
        return Optional.ofNullable(this.getItem(id))
                .orElseThrow(()-> new RuntimeException("can not find typeDefinition by id:"+id));
    }
    public TypeDefinition getMust(TypeItem typeItem) {
        return this.getMust(typeItem.getItemId());
    }

    public List<TypeDefinition> getAllItems() {
        return this.sortedAllItems.stream().map(this::getMust).toList();
    }
    public void addItem(TypeDefinition item) {
        if (item.getScope().isPublic()){
            this.publicItems.put(item.getId(),item);
        }else{
            this.privateItems.put(item.getId(),item);
        }
    }
    public boolean contains(String id) {
        return Optional.ofNullable(this.getItem(id)).isPresent();
    }

    public boolean existsInPublic(String id) {
        return this.publicItems.containsKey(id);
    }
}
