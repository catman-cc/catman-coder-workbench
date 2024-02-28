package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeDefinitionSchema {

    private String root;

    @Builder.Default
    private Map<String, TypeDefinition> definitions = new HashMap<>();
    @Builder.Default
    private Map<String, List<String>> refs = new HashMap<>();
    @Builder.Default
    private ILoopReferenceContext context=ILoopReferenceContext.create();

    public static TypeDefinitionSchema of(TypeDefinition typeDefinition) {
        TypeDefinitionSchema schema = new TypeDefinitionSchema();
        schema.setRoot(typeDefinition.getId());
        if (Optional.ofNullable(typeDefinition.getType().getContext()).isPresent()){
            schema.setContext(typeDefinition.getType().getContext());
        }
        return schema.parse(typeDefinition);
    }

    public List<String> getAllReference(String id) {
        return this.refs.entrySet().stream()
                .filter(e -> e.getValue().contains(id))
                .map(Map.Entry::getKey).toList();
    }

    public TypeDefinition toTypeDefinition() {
        TypeDefinition root = definitions.get(this.root);
        root.getContext().addTypeDefinitions(definitions.values());
        return root;
    }


    public TypeDefinitionSchema parse(TypeDefinition typeDefinition) {
        if (typeDefinition.getScope().isPublic()){
            // 公开类型
            // 解析类型定义,需要优先验证缓存
            if (this.getDefinitions().containsKey(typeDefinition.getId())) {
                // 如果已经存在于schema中,无需再次加入和解析
                return this;
            }
            // 缓存中不存在资源,则需要解析
            this.getDefinitions().put(typeDefinition.getId(), typeDefinition);
        }
        // 然后递归的将所有的子类型定义都加入到schema中
        typeDefinition.getAllItems().forEach((item)->{
            this.getRefs().compute(typeDefinition.getId(), (k, v) -> {
                if (v == null) {
                    List<String> list = new ArrayList<>();
                    list.add(item.getId());
                    return list;
                } else {
                    v.add(item.getId());
                    return v;
                }
            });
            this.parse(item);
        });
        return this;
    }
}
