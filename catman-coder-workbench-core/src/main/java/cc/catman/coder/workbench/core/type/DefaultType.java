package cc.catman.coder.workbench.core.type;

import java.util.*;
import java.util.function.Function;

import cc.catman.coder.workbench.core.DefaultLoopReferenceContext;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.entity.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @Builder.Default
    protected List<TypeItem> sortedAllItems = new ArrayList<>();

    @Getter
    @Setter
    @Builder.Default
    @JsonIgnore
    protected transient ILoopReferenceContext context= DefaultLoopReferenceContext.create();

    public DefaultType add(TypeDefinition typeDefinition) {
//        privateItems.put(typeDefinition.getId(),typeDefinition);
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


    @JsonIgnore
    public TypeDefinition getItem(String id) {
        return this.context.getTypeDefinition(id).orElse(null);
//       return Optional.ofNullable( this.privateItems.get(id))
//               .orElseGet(()-> this.context.getTypeDefinition(id).orElse(null));
    }
    @JsonIgnore
    public TypeDefinition getItem(TypeItem typeItem) {
        return this.getItem(typeItem.getItemId());
    }
    @JsonIgnore
    public TypeDefinition getMust(String id) {
        return Optional.ofNullable(this.getItem(id))
                .orElseThrow(()-> new RuntimeException("can not find typeDefinition by id:"+id));
    }
    @JsonIgnore
    public TypeDefinition getMust(TypeItem typeItem) {
        return this.getMust(typeItem.getItemId());
    }
    @JsonIgnore
    public List<TypeDefinition> getAllItems() {
        return this.sortedAllItems.stream().map(this::getItem).toList();
    }
    public void addItem(TypeDefinition item) {
        if (item == null) {
            return;
        }
        this.context.add(item);
        if(this.sortedAllItems.stream().anyMatch(i->i.getItemId().equals(item.getId()))){
            return;
        }
        this.sortedAllItems.add(TypeItem.builder().itemId(item.getId()).build());

//        if (item.getScope().isPublic()){
//           this.context.add(item);
//        }else{
//            this.privateItems.put(item.getId(),item);
//        }
    }
    public boolean contains(String id) {
        return Optional.ofNullable(this.getItem(id)).isPresent();
    }

    public boolean existsInPublic(String id) {
        return this.context.includeTypeDefinition(id);
    }
}
