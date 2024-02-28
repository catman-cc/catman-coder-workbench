package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.TypeDefinitionSchema;
import cc.catman.coder.workbench.core.type.TypeItem;
import cc.catman.workbench.service.core.services.ITypeDefinitionSchemaService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TypeDefinitionSchemaServiceImpl implements ITypeDefinitionSchemaService {

    @Resource
    private ITypeDefinitionService typeDefinitionService;

    @Override
    @Transactional
    public TypeDefinitionSchema save(TypeDefinitionSchema schema) {
        if (schema == null) {
            throw new RuntimeException("schema can not be null");
        }

        ILoopReferenceContext context= Optional.of(schema).map(TypeDefinitionSchema::getContext).orElse(ILoopReferenceContext.create());
        context.addTypeDefinitions(new ArrayList<>(Optional.ofNullable(schema.getDefinitions()).orElse(Collections.emptyMap()).values()));
        // 接下来开始执行保存操作
        Map<String, TypeDefinition> map = schema.getDefinitions().values().stream().map(typeDefinition -> {
            // 这里限制了,禁止修改其他public的类型定义,如果解除此处的限制,则可以修改了
            if (typeDefinition.getScope().isPublic()) {
                if (typeDefinition.getId().equals(schema.getRoot())) {
                    // 如果是根节点,那么就可以修改
                    return typeDefinitionService.save(typeDefinition);
                }
                // 其他执行一次查询操作,确保返回的是最新的数据
                return typeDefinitionService
                        .findById(typeDefinition.getId())
                        .orElseThrow(() -> new RuntimeException("can not find type definition:" + typeDefinition.getId()));
            }
            return typeDefinitionService.save(typeDefinition);
        }).collect(Collectors.toMap(
                TypeDefinition::getId,
                d -> d
        ));
//        schema.setDefinitions(map);
//        return schema;

        return findById(schema.getRoot()).orElseThrow();
    }

    @Override
    public Optional<TypeDefinitionSchema> findById(String id) {
        return Optional.ofNullable(typeDefinitionService.findById(id).orElseThrow(() -> new RuntimeException("can not find type definition:" + id)))
                .map(TypeDefinitionSchema::of);
    }

    public TypeDefinitionSchema findById(String id, TypeDefinitionSchema schema) {
        TypeDefinition typeDefinition = typeDefinitionService.findById(id).orElseThrow(() -> new RuntimeException("can not find type definition:" + id));
        schema.getDefinitions().put(id, typeDefinition);
        Map<String, List<String>> referenceRelationship = schema.getRefs();
        DefaultType type = typeDefinition.getType();
        for (TypeItem item : type.getSortedAllItems()) {
            String itemId = item.getItemId();
            referenceRelationship.compute(id, (k, v) -> {
                if (v == null) {
                    List<String> list = new ArrayList<>();
                    list.add(itemId);
                    return list;
                } else {
                    v.add(itemId);
                    return v;
                }
            });
            deepFillPublicDefinitions(typeDefinition, schema);
        }
        return schema;
    }

    public  TypeDefinition deepFillPublicDefinitions(TypeDefinition typeDefinition,TypeDefinitionSchema schema){
        DefaultType type = typeDefinition.getType();
        for (TypeItem item : type.getSortedAllItems()) {
            String itemId = item.getItemId();
             type.getPrivateItems().values().stream().filter(i -> i.getId().equals(itemId)).findFirst()
                    .map(i->deepFillPublicDefinitions(i,schema))
                    .orElseGet(()-> Optional.ofNullable(schema.getDefinitions().get(itemId))
                            .orElseGet(()->{
                                TypeDefinition itemDefinition = typeDefinitionService.findById(itemId).orElseThrow(() -> new RuntimeException("can not find type definition:" + itemId));
                                schema.getDefinitions().put(itemId, itemDefinition);
                                deepFillPublicDefinitions(itemDefinition,schema);
                                return itemDefinition;
                            }));
        }
        return typeDefinition;
    }
}
