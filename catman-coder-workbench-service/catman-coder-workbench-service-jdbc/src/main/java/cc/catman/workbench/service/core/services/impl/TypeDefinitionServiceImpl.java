package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.SimpleInfo;
import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.plugin.core.label.filter.AndLabelFilter;
import cc.catman.plugin.core.label.filter.LabelFilterBuilder;
import cc.catman.workbench.service.core.po.TypeDefinitionPO;
import cc.catman.workbench.service.core.po.TypeDefinitionTypeItemRef;
import cc.catman.workbench.service.core.po.TypeDefinitionTypeRef;
import cc.catman.workbench.service.core.repossitory.ITypeDefinitionRepository;
import cc.catman.workbench.service.core.repossitory.ITypeDefinitionTypeItemRefRepository;
import cc.catman.workbench.service.core.repossitory.ITypeDefinitionTypeRefRepository;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import com.github.benmanes.caffeine.cache.Cache;
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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TypeDefinitionServiceImpl implements ITypeDefinitionService {
    @Resource
    private ModelMapper modelMapper;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private Cache<String, TypeDefinition> caffeineCache;

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

        // 保存base信息
        Base newBaseInfo = baseService.save(modelMapper.map(typeDefinition, Base.class), "td", typeDefinition.getId());
        if (log.isInfoEnabled()) {
            log.info("保存类型定义的基础数据:{}", newBaseInfo);
        }
        newBaseInfo.mergeInto(typeDefinition);


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
        AtomicInteger order = new AtomicInteger(0);
        TypeDefinitionTypeRef finalTypeDefinitionTypeRef = typeDefinitionTypeRef;
        // 当类型为集合时,需要自动填充item类型
        if (Constants.Type.TYPE_NAME_ARRAY.equals(type.getTypeName())) {
            // 此处需要校验
            if (CollectionUtils.isEmpty(type.getItems())) {
                type.setItems(Collections.singletonList(TypeDefinition.builder().name("elements").type(new StringRawType()).build()));
            } else if (type.getItems().size() > 1) {
                throw new IllegalArgumentException("数组类型只能有一个元素");
            } else if (!Objects.equals(type.getItems().get(0).getName(), "elements")) {
                throw new IllegalArgumentException("数组类型的元素名称必须为elements");
            }
        }

        typeDefinition.getType().setItems(typeDefinition.getType().getItems().stream().map(item -> {
            // 递归保存
            TypeDefinition childTypeDefinition = save(item);
            TypeDefinitionTypeItemRef typeDefinitionTypeItemRef = TypeDefinitionTypeItemRef.builder()
                    .typeDefinitionTypeId(finalTypeDefinitionTypeRef.getId())
                    .referencedTypeDefinitionId(childTypeDefinition.getId())
                    .orderIndex(order.incrementAndGet())
                    .build();

            TypeDefinitionTypeItemRef savedTypeDefinitionTypeItemRef = typeDefinitionTypeItemRefRepository.saveAndFlush(typeDefinitionTypeItemRef);
            if (log.isInfoEnabled()) {
                log.info("保存类型定义的关联数据:{},关联关系:{}", childTypeDefinition, savedTypeDefinitionTypeItemRef);
            }
            return childTypeDefinition;
        }).toList());

        // 更新缓存
        caffeineCache.put(typeDefinition.getId(), typeDefinition);
        return typeDefinition;
    }

    @Override
    public Optional<TypeDefinition> findById(String id) {
        return Optional.ofNullable(caffeineCache.getIfPresent(id))
                .or(() -> typeDefinitionRepository.findById(id).map(this::convertToTypeDefinition));
    }

    protected TypeDefinition convertToTypeDefinition(TypeDefinitionPO typeDefinitionPO) {
        return caffeineCache.get(typeDefinitionPO.getId(), (key) -> {
                    TypeDefinition typeDefinition = modelMapper.map(typeDefinitionPO, TypeDefinition.class);
                    // 处理类型数据
                    return typeDefinitionTypeRefRepository
                            .findOne(Example.of(TypeDefinitionTypeRef.builder().typeDefinitionId(typeDefinitionPO.getId()).build()))
                            .map(typeDefinitionTypeRef -> {
                                String typeName = typeDefinitionTypeRef.getTypeName();
                                // 继续查找子类型定义
                                List<TypeDefinition> items = typeDefinitionTypeItemRefRepository
                                        .findAll(
                                                Example.of(
                                                        TypeDefinitionTypeItemRef
                                                                .builder()
                                                                .typeDefinitionTypeId(typeDefinitionTypeRef.getId()
                                                                )
                                                                .build())
                                                , Sort.by(Sort.Direction.ASC, "orderIndex")
                                        )
                                        .stream()
                                        .map(itemRef -> findById(itemRef.getReferencedTypeDefinitionId())
                                                .orElse(null)
                                        )
                                        .toList();
                                DefaultType type = conversionService.convert(DefaultType.builder()
                                        .typeName(typeName)
                                        .items(items).build(), DefaultType.class);

                                typeDefinition.setType(type);
                                // 处理base数据
                                Base baseInfo = baseService.findBaseByBelongId(typeDefinitionPO.getId());
                                return baseInfo.mergeInto(typeDefinition);
                            }).orElse(null);
                }
        );
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
                .map(this::convertToTypeDefinition)
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
        return null;
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
        return typeDefinitionTypeRefRepository.findOne(Example.of(TypeDefinitionTypeRef.builder().typeDefinitionId(id).build())).map(typeRef-> typeDefinitionTypeItemRefRepository.findAll(Example.of(TypeDefinitionTypeItemRef.builder().typeDefinitionTypeId(typeRef.getId()).build()))
                .stream()
                .map(itemRef->findById(itemRef.getReferencedTypeDefinitionId()).orElse(null))
                .toList()).orElse(Collections.emptyList());
    }
}
