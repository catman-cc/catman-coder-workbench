package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.SimpleInfo;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.type.*;
import cc.catman.plugin.core.label.filter.AndLabelFilter;
import cc.catman.plugin.core.label.filter.LabelFilterBuilder;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.workbench.service.core.po.TypeDefinitionPO;
import cc.catman.workbench.service.core.po.TypeDefinitionTypeItemRef;
import cc.catman.workbench.service.core.po.TypeDefinitionTypeRef;
import cc.catman.workbench.service.core.repossitory.ITypeDefinitionRepository;
import cc.catman.workbench.service.core.repossitory.ITypeDefinitionTypeItemRefRepository;
import cc.catman.workbench.service.core.repossitory.ITypeDefinitionTypeRefRepository;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.IdGenerator;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TypeDefinitionServiceImpl implements ITypeDefinitionService {
    @Resource
    private ModelMapper modelMapper;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private ConversionService conversionService;

    @Resource
    private IBaseService baseService;
    @Resource
    private ITypeDefinitionRepository typeDefinitionRepository;

    @Resource
    private ITypeDefinitionTypeRefRepository typeDefinitionTypeRefRepository;
    @Resource
    private ITypeDefinitionTypeItemRefRepository typeDefinitionTypeItemRefRepository;

    @Override
    public TypeDefinition save(TypeDefinition typeDefinition) {
        if (log.isInfoEnabled()) {
            log.info("保存类型定义:{}", typeDefinition);
        }

        // 查找是否已存在对应的类型定义,如果存在,则判断是否需要更新本体数据
        TypeDefinitionPO currentTypeDefinitionPO = modelMapper.map(typeDefinition, TypeDefinitionPO.class);
        TypeDefinitionPO oldTypeDefinitionPO = typeDefinitionRepository.findById(typeDefinition.getId()).orElseGet(() -> TypeDefinitionPO.builder().build());

        boolean needUpdate = !currentTypeDefinitionPO.equals(oldTypeDefinitionPO);
        if (log.isInfoEnabled()) {
            log.info("类型定义是否需要更新:{},现有类型定义:{}", needUpdate, currentTypeDefinitionPO);
        }
        if (needUpdate) {
            // 表示基础数据发生变化,需要执行保存操作
            currentTypeDefinitionPO = typeDefinitionRepository.saveAndFlush(currentTypeDefinitionPO);
            typeDefinition.setId(currentTypeDefinitionPO.getId());
        }

        // 调用基础服务,保存通用基础信息
        // 保存base信息
        Base newBaseInfo = baseService.save(modelMapper.map(typeDefinition, Base.class), "td", typeDefinition.getId());
        if (log.isInfoEnabled()) {
            log.info("保存类型定义的基础数据:{}", newBaseInfo);
        }
        newBaseInfo.mergeInto(typeDefinition);


        // 接下来开始处理具体的类型定义
        DefaultType type = typeDefinition.getType();
        // 比较类型定义关联是否有变化
        TypeDefinitionTypeRef typeDefinitionTypeRef = typeDefinitionTypeRefRepository
                .findOne(
                        Example.of(TypeDefinitionTypeRef.builder().typeDefinitionId(currentTypeDefinitionPO.getId()).build())
                )
                .orElseGet(() -> (TypeDefinitionTypeRef.builder().id(idGenerator.generateId().toString()).typeName("").build()));

        String typeName = typeDefinitionTypeRef.getTypeName();
        if (!typeName.equals(type.getTypeName())) {
            // 表示类型定义关联发生变化,需要执行保存操作
            typeDefinitionTypeRef = typeDefinitionTypeRefRepository.saveAndFlush(TypeDefinitionTypeRef.builder()
                    .id(typeDefinitionTypeRef.getId())
                    .typeDefinitionId(currentTypeDefinitionPO.getId())
                    .typeName(type.getTypeName())
                    .build());
        }

        if (log.isInfoEnabled()) {
            log.info("保存类型定义的关联数据:{}", typeDefinitionTypeRef);
        }

        // 如果当前TypeDefinition属于复合类型定义,那么他将存在子类型定义,需要将子类型定义保存到数据库中
        // 这里选择的策略是,先移除所有的关联关系,然后重新构建关联关系
        Example<TypeDefinitionTypeItemRef> deleteTypeDefinitionTypeItemRefs = Example
                .of(
                        TypeDefinitionTypeItemRef
                                .builder()
                                .typeDefinitionTypeId(typeDefinitionTypeRef.getId())
                                .build()
                );

        if (log.isInfoEnabled()) {
            log.info("删除类型定义的关联数据:{}", deleteTypeDefinitionTypeItemRefs);
        }
        // 删除类型定义关联
        typeDefinitionTypeItemRefRepository.deleteAll(typeDefinitionTypeItemRefRepository.findAll(deleteTypeDefinitionTypeItemRefs));

        // 重新构建关联关系
        // 从全量的子类型定义中,依次构建关联关系,这里需要根据具体的类型定义进行处理

        // 基础数据类型,其不存在子类型定义,所以不需要处理
        // 集合引用类型,理论上只可以存在一个子类型定义,所以这里需要校验
        // 对象类型,其子类型定义可以是任意类型,所以需要递归处理
        // 引用类型和泛型类型,其子类型定义可以是任意类型,所以需要递归处理

        // 综上所述,先进行集合类型的校验
        // 当类型为集合时,需要自动填充item类型
        List<TypeItem> sortedItems = type.getSortedAllItems();
        if (Constants.Type.TYPE_NAME_ARRAY.equals(type.getTypeName())) {
            // 此处需要校验
            if (CollectionUtils.isEmpty(type.getSortedAllItems())) {
                // 集合类型有且仅有一个子元素类型定义,如果没有提供子元素类型定义,为了保证最大的包容性,将为其设置为any类型
                TypeDefinition anyType = TypeDefinition.builder()
                        .name("elements").type(new AnyType()).build();
                anyType.setId(idGenerator.generateId().toString());
                type.getPrivateItems().put(anyType.getId(), anyType);
                type.getSortedAllItems().add(TypeItem.builder()
                        .name("elements")
                        .itemId(anyType.getId())
                        .build());
            } else if (sortedItems.size() > 1) {
                throw new IllegalArgumentException("数组类型只能有一个元素");
            } else if (sortedItems.stream().filter(item -> !item.getName().equals("elements")).count() > 0) {
                throw new IllegalArgumentException("数组类型的元素名称必须为elements");
            }
        }

        // 接下来是处理子元素类型定义,首先排除掉基础类型,因为基础类型不持有子类型定义
        ETypeName eType = ETypeName.from(type.getTypeName());
        if (eType.isRaw()) {
            // 基础类型不持有子类型定义,校验是否存在子元素类型定义
            if (!CollectionUtils.isEmpty(type.getSortedAllItems())) {
                log.warn("基础类型不持有子类型定义,存在的子元素类型定义,将被忽略,{}", type.getSortedAllItems());
            }
        }
        Map<String, TypeDefinition> privateItems = type.getPrivateItems();

        // 注意,所有的子类型定义,都必须存在id,否则无法进行关联
        Map<String, TypeDefinition> privateItemMap = privateItems.values().stream()
                .peek(t -> {
                    if (Objects.isNull(t.getId())) {
                        t.setId(idGenerator.generateId().toString());
                    }
                })
                .collect(Collectors.toMap(TypeDefinition::getId, (t) -> t));

        Map<String, TypeDefinition> newPrivateItems = new HashMap<>();
        for (int i = 0; i < type.getSortedAllItems().size(); i++) {
            TypeItem typeItem = type.getSortedAllItems().get(i);
            if (!Scope.PUBLIC.equals(typeItem.getItemScope())) {
                // 私有类型定义,需要进行保存操作.
                if (!Objects.isNull(typeItem.getItemId())) {
                    // 校验是否存在name
                    if (Objects.isNull(typeItem.getName())) {
                        throw new IllegalArgumentException("类型定义的子元素必须存在id或者name");
                    }
                    // 判断是否为私有类型定义
                    if (privateItemMap.containsKey(typeItem.getItemId())) {
                        // 说明是一个私有类型定义,需要执行保存操作
                        TypeDefinition saved = save(privateItemMap.get(typeItem.getItemId()));
                        typeItem.setItemId(saved.getId());
                        newPrivateItems.put(saved.getId(), saved);
                    } else {
                        // 说明是一个公开类型定义,不需要执行保存操作,但是需要验证引用的类型定义是否存在
                        findById(typeItem.getItemId()).orElseThrow(() -> new IllegalArgumentException("无法找到被引用的的类型定义,id:" + typeItem.getItemId()));
                        // 更新作用域
                        typeItem.setItemScope(Scope.PUBLIC);
                    }
                } else if (!Objects.isNull(typeItem.getName())) {
                    // 说明是一个私有类型定义,需要执行保存操作
                    // 根据name尝试获取类型定义
                    TypeDefinition typeDefinitionByName = privateItems.values().stream()
                            .filter(item -> typeItem.getName().equals(item.getName()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("无法找到被引用的的类型定义,name:" + typeItem.getName()));
                    TypeDefinition saved = save(typeDefinitionByName);
                    typeItem.setItemId(saved.getId());
                    newPrivateItems.put(saved.getId(), saved);
                } else {
                    throw new IllegalArgumentException("类型定义的子元素必须存在id或者name");
                }
            }
            // 保存关联关系
            TypeDefinitionTypeItemRef typeDefinitionTypeItemRef = TypeDefinitionTypeItemRef.builder()
                    .name(typeItem.getName())
                    .typeDefinitionTypeId(typeDefinitionTypeRef.getId())
                    .referencedTypeDefinitionId(typeItem.getItemId())
                    .orderIndex(i)
                    .build();
            TypeDefinitionTypeItemRef tdtir = typeDefinitionTypeItemRefRepository.saveAndFlush(typeDefinitionTypeItemRef);
            if (log.isInfoEnabled()) {
                log.info("保存类型定义的关联数据:{},关联关系:{}", typeItem, tdtir);
            }
        }
        typeDefinition.getType().setPrivateItems(newPrivateItems);
        // 更新缓存
//        caffeineCache.put(typeDefinition.getId(), typeDefinition);
        return typeDefinition;
    }

    @Override
    public Optional<TypeDefinition> findById(String id) {
        return typeDefinitionRepository.findById(id).map(typeDefinitionPO -> this.convertToTypeDefinition(typeDefinitionPO,new HashMap<>()));
    }

    @Override
    public Optional<TypeDefinition> findById(String id, TypeDefinition parent) {
        if (Optional.ofNullable(parent).isEmpty()){
            return findById(id);
        }
        if (parent.existsInPublic(id)){
            return Optional.ofNullable(parent.getPublic(id));
        }
        return typeDefinitionRepository
                .findById(id)
                .map(typeDefinitionPO -> this.convertToTypeDefinition(typeDefinitionPO,parent.getType().getPublicItems()));
    }

    @Override
    public Optional<TypeDefinition> findById(String id, ILoopReferenceContext context) {
        return context.getTypeDefinition(id,
                (ctx)-> typeDefinitionRepository
                        .findById(id)
                        .map(typeDefinitionPO -> this.convertToTypeDefinition(typeDefinitionPO,ctx)));
    }

    @Override
    public Optional<TypeDefinition> findById(String id, Map<String, TypeDefinition> existPublicTypeDefinitions) {
        if (existPublicTypeDefinitions.containsKey(id)){
            return Optional.ofNullable(existPublicTypeDefinitions.get(id));
        }
        return typeDefinitionRepository.findById(id).map(typeDefinitionPO -> this.convertToTypeDefinition(typeDefinitionPO,existPublicTypeDefinitions));
    }

    protected TypeDefinition convertToTypeDefinition(TypeDefinitionPO typeDefinitionPO,ILoopReferenceContext context) {
        TypeDefinition typeDefinition = modelMapper.map(typeDefinitionPO, TypeDefinition.class);
        context.add(typeDefinition);
        Map<String,TypeDefinition> existTypeDefinitions=context.getTypeDefinitions();

        // 处理类型数据
        return typeDefinitionTypeRefRepository
                .findOne(Example.of(TypeDefinitionTypeRef.builder().typeDefinitionId(typeDefinitionPO.getId()).build()))
                .map(typeDefinitionTypeRef -> {
                    String typeName = typeDefinitionTypeRef.getTypeName();
                    Map<String, TypeDefinition> privateItems = new HashMap<>();
                    List<TypeItem> sortedAllItems = new ArrayList<>();
                    DefaultType type = conversionService.convert(DefaultType.builder()
                            .typeName(typeName)
                            .context(context)
                            .publicItems(existTypeDefinitions)
                            .sortedAllItems(sortedAllItems)
                            .privateItems(privateItems).build(), DefaultType.class);
                    typeDefinition.setType(type);

                    // 继续查找子类型定义
                    // 获取所有被引用的类型定义
                    List<TypeDefinitionTypeItemRef> typeItems = typeDefinitionTypeItemRefRepository
                            .findAll(
                                    Example.of(
                                            TypeDefinitionTypeItemRef
                                                    .builder()
                                                    .typeDefinitionTypeId(typeDefinitionTypeRef.getId()
                                                    )
                                                    .build())
                                    , Sort.by(Sort.Direction.ASC, "orderIndex")
                            );

                    typeItems.forEach(typeItem -> {
                        TypeItem newItem = TypeItem.builder()
                                .name(typeItem.getName())
                                .itemId(typeItem.getReferencedTypeDefinitionId())
                                .itemScope(Optional.ofNullable(typeItem.getItemScope()).map(Scope::valueOf).orElse(Scope.PRIVATE))
                                .build();

                        sortedAllItems.add(newItem);

                        if (!typeDefinition.contains(newItem.getItemId())) {
                            TypeDefinitionPO tpo = typeDefinitionRepository.findById(typeItem.getReferencedTypeDefinitionId())
                                    .orElseThrow(() -> new IllegalArgumentException("无法找到被引用的的类型定义,id:" + typeItem.getReferencedTypeDefinitionId()));
                            TypeDefinition item = convertToTypeDefinition(tpo,context);
                            typeDefinition.addItem(item);
                        }
                    });

                    // 处理base数据
                    Base baseInfo = baseService.findByKindAndBelongId("typeDefinition", typeDefinitionPO.getId());
                    return Optional.ofNullable(baseInfo).map(base -> {
                        base.mergeInto(typeDefinition);
                        return typeDefinition;
                    }).orElse(typeDefinition);
                }).orElse(null);
    }

    protected TypeDefinition convertToTypeDefinition(TypeDefinitionPO typeDefinitionPO,Map<String,TypeDefinition> existTypeDefinitions) {
        TypeDefinition typeDefinition = modelMapper.map(typeDefinitionPO, TypeDefinition.class);
        // 处理类型数据
        return typeDefinitionTypeRefRepository
                .findOne(Example.of(TypeDefinitionTypeRef.builder().typeDefinitionId(typeDefinitionPO.getId()).build()))
                .map(typeDefinitionTypeRef -> {
                    String typeName = typeDefinitionTypeRef.getTypeName();
                    Map<String, TypeDefinition> privateItems = new HashMap<>();
                    List<TypeItem> sortedAllItems = new ArrayList<>();
                    DefaultType type = conversionService.convert(DefaultType.builder()
                            .typeName(typeName)
                            .publicItems(existTypeDefinitions)
                            .sortedAllItems(sortedAllItems)
                            .privateItems(privateItems).build(), DefaultType.class);
                    typeDefinition.setType(type);

                    // 继续查找子类型定义
                    // 获取所有被引用的类型定义
                    List<TypeDefinitionTypeItemRef> typeItems = typeDefinitionTypeItemRefRepository
                            .findAll(
                                    Example.of(
                                            TypeDefinitionTypeItemRef
                                                    .builder()
                                                    .typeDefinitionTypeId(typeDefinitionTypeRef.getId()
                                                    )
                                                    .build())
                                    , Sort.by(Sort.Direction.ASC, "orderIndex")
                            );

                    typeItems.forEach(typeItem -> {
                        TypeItem newItem = TypeItem.builder()
                                .name(typeItem.getName())
                                .itemId(typeItem.getReferencedTypeDefinitionId())
                                .itemScope(Optional.ofNullable(typeItem.getItemScope()).map(Scope::valueOf).orElse(Scope.PRIVATE))
                                .build();
                        sortedAllItems.add(newItem);
                        if (!typeDefinition.contains(newItem.getItemId())) {
                            TypeDefinitionPO tpo = typeDefinitionRepository.findById(typeItem.getReferencedTypeDefinitionId())
                                    .orElseThrow(() -> new IllegalArgumentException("无法找到被引用的的类型定义,id:" + typeItem.getReferencedTypeDefinitionId()));
                            TypeDefinition item = convertToTypeDefinition(tpo,existTypeDefinitions);
                            typeDefinition.addItem(item);
                        }
                    });

                    // 处理base数据
                    Base baseInfo = baseService.findByKindAndBelongId("typeDefinition", typeDefinitionPO.getId());
                    return Optional.ofNullable(baseInfo).map(base -> {
                        base.mergeInto(typeDefinition);
                        return typeDefinition;
                    }).orElse(typeDefinition);
                }).orElse(null);
    }

    @Override
    public Optional<TypeDefinition> deleteById(String id) {
        // 删除类型定义时,需要考虑类型定义的范围,如果类型定义是PRIVATE,那么可以删除,如果类型是PUBLIC,在级联状态下,删除级联关系即可
        typeDefinitionRepository.deleteById(id);
        baseService.deleteByKindAndBelongId("td", id);
        // 移除管理数据
        deleteByBelongId(id);
        return Optional.empty();
    }

    @Override
    public Optional<TypeDefinition> deleteByBelongId(String belongId) {
        typeDefinitionTypeRefRepository.findOne(Example.of(TypeDefinitionTypeRef.builder()
                .typeDefinitionId(belongId)
                .build())).ifPresent(typeDefinitionTypeRef -> {
            typeDefinitionTypeItemRefRepository
                    .findAll(Example.of(TypeDefinitionTypeItemRef.builder().typeDefinitionTypeId(typeDefinitionTypeRef.getId()).build()))
                    .forEach(typeDefinitionTypeItemRef -> {
                        // 处理获取的每一个子类型定义
                        findById(typeDefinitionTypeItemRef.getReferencedTypeDefinitionId()).ifPresent(typeDefinition -> {
                            // 递归删除
                            if (Scope.PRIVATE.equals(typeDefinition.getScope())) {
                                deleteById(typeDefinitionTypeItemRef.getReferencedTypeDefinitionId());
                            } else {
                                // 公开的类型定义,删除关联关系即可
                                typeDefinitionTypeItemRefRepository.delete(typeDefinitionTypeItemRef);
                            }
                        });
                    });

        });

        return Optional.empty();
    }

    @Override
    public List<TypeDefinition> list(TypeDefinition typeDefinition) {
        return typeDefinitionRepository.findAll()
                .stream()
                .map(v->this.convertToTypeDefinition(v,new HashMap<>()))
                .filter(td -> Scope.PUBLIC.equals(td.getScope()))
                .toList();
    }

    @Override
    public long count(String id) {
        list(null).stream()
                .filter(td -> Scope.PUBLIC.equals(td.getScope()))
                .filter(td -> {
                    // 根据标签进行过滤
                    return LabelFilterBuilder
                            .create()
                            .and()
                            .and(AndLabelFilter.of(l -> {
                                // 使用el表达式,解析
                                return l.labels().contain("id");
                            }))
                            .end()
                            .build()
                            .filter(td.getLabels());
                })
                .count();
        return 0;
    }

    @Override
    public List<SimpleInfo> listSimple() {
        return this.typeDefinitionRepository.findAll(Example.of(TypeDefinitionPO.builder()
                .scope(Scope.PUBLIC.name())
                .build())).stream().map((td) -> {
            SimpleInfo simpleInfo = new SimpleInfo();
            simpleInfo.setId(td.getId());
            simpleInfo.setName(td.getName());
            simpleInfo.setScope(Scope.valueOf(td.getScope()));
            return simpleInfo;
        }).toList();
    }

    @Override
    public boolean updateScope(String id, String scope) {
        baseService.updateScopeByKindAndBelongID("", id, scope);
        return true;
    }

    @Override
    public boolean canBeAssigned(TypeDefinition typeDefinition, TypeDefinition target) {
        return typeDefinition.getType().canConvert(target.getType());
    }

    @Override
    public List<TypeDefinition> findDirectReferencedById(String id) {
        return typeDefinitionTypeRefRepository.findOne(Example.of(TypeDefinitionTypeRef.builder().typeDefinitionId(id).build())).map(typeRef -> typeDefinitionTypeItemRefRepository.findAll(Example.of(TypeDefinitionTypeItemRef.builder().typeDefinitionTypeId(typeRef.getId()).build()))
                .stream()
                .map(itemRef -> findById(itemRef.getReferencedTypeDefinitionId()).orElse(null))
                .toList()).orElse(Collections.emptyList());
    }
}
