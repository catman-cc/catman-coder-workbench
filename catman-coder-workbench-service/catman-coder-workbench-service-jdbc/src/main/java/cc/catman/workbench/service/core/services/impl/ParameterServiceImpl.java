package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.po.ParameterItemRef;
import cc.catman.workbench.service.core.po.ParameterRef;
import cc.catman.workbench.service.core.repossitory.IParameterItemRefRepository;
import cc.catman.workbench.service.core.repossitory.IParameterRefRepository;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.services.IParameterService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import cc.catman.workbench.service.core.services.IValueProviderDefinitionService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ParameterServiceImpl implements IParameterService {
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private IParameterRefRepository parameterRefRepository;
    @Resource
    private IParameterItemRefRepository parameterItemRefRepository;

    @Resource
    private IBaseService baseService;
    @Resource
    private ITypeDefinitionService typeDefinitionService;
    @Lazy
    @Resource
    private IValueProviderDefinitionService valueProviderDefinitionService;

    @Override
    public Optional<Parameter> createFromTypeDefinition(TypeDefinition typeDefinition) {
        return createFromTypeDefinition(null, typeDefinition);
    }

    @Override
    public Optional<Parameter> findById(String id) {
        return Optional.ofNullable(id).flatMap(pid -> parameterRefRepository.findById(pid).map(parameterRef -> {
            Parameter parameter = modelMapper.map(parameterRef, Parameter.class);

            // 填充类型定义
            typeDefinitionService.findById(parameterRef.getTypeDefinitionId())
                    .ifPresent(parameter::setType);

            // 填充值提取器
            Optional.ofNullable(parameterRef.getValueProviderDefinitionId()).flatMap(valueProviderDefinitionId -> valueProviderDefinitionService.findById(valueProviderDefinitionId)).ifPresent(parameter::setValue);
            // 填充默认值提取器

            Optional.ofNullable(parameterRef.getDefaultValueProviderDefinitionId()).flatMap(defaultValueProviderDefinitionId -> valueProviderDefinitionService.findById(defaultValueProviderDefinitionId)).ifPresent(parameter::setDefaultValue);


            // 填充子元素
            parameterItemRefRepository.findAll(
                            Example.of(
                                    ParameterItemRef.builder().belongParameterId(parameter.getId()).build()
                            )
                    )
                    .stream()
                    .sorted(Comparator.comparing(ParameterItemRef::getOrderIndex))
                    .map(itemRef -> {
                        // 填充子元素的类型定义
                        return findById(itemRef.getReferencedParameterId(),parameter.getPublicValueProviderDefinitions(),parameter.getPublicItems(),parameter.getPublicTypeDefinitions()).orElseThrow();
                    })
                    .forEach(parameter.getItems()::add);
            // 填充base
            Optional.ofNullable(baseService.findByKindAndBelongId("parameter", parameter.getId())).ifPresent(base -> {
                base.mergeInto(parameter);
            });
            return parameter;
        }));
    }

    @Override
    public Optional<Parameter> findById(String id, ILoopReferenceContext context) {
        return context.getParameter(id,(ctx)->{
            return Optional.ofNullable(id).flatMap(pid -> parameterRefRepository.findById(pid).map(parameterRef -> {
                Parameter parameter = modelMapper.map(parameterRef, Parameter.class);
                parameter.setPublicItems(context.getParameters());
                parameter.setPublicTypeDefinitions(context.getTypeDefinitions());
                context.add(parameter);


                // 填充类型定义
                typeDefinitionService.findById(parameterRef.getTypeDefinitionId(),context)
                        .ifPresent(parameter::setType);

                // 填充值提取器
                valueProviderDefinitionService.findById(parameterRef.getValueProviderDefinitionId(),context)
                        .ifPresent(parameter::setValue);
                // 填充默认值提取器
                valueProviderDefinitionService.findById(parameterRef.getDefaultValueProviderDefinitionId(),context)
                        .ifPresent(parameter::setDefaultValue);

                // 填充子元素
                parameterItemRefRepository.findAll(
                                Example.of(
                                        ParameterItemRef.builder().belongParameterId(parameter.getId()).build()
                                )
                        )
                        .stream()
                        .sorted(Comparator.comparing(ParameterItemRef::getOrderIndex))
                        .map(itemRef -> {
                            // 填充子元素的类型定义
                            return findById(itemRef.getReferencedParameterId(),context).orElseThrow();
                        })
                        .forEach(parameter.getItems()::add);
                // 填充base
                Optional.ofNullable(baseService.findByKindAndBelongId("parameter", parameter.getId())).ifPresent(base -> {
                    base.mergeInto(parameter);
                });
                return parameter;
            }));
        });
    }

    @Override
    public Optional<Parameter> findById(String id, Map<String, ValueProviderDefinition> existPublicValueProviderDefinitions, Map<String, Parameter> existPublicParameters, Map<String, TypeDefinition> existPublicTypeDefinitions) {


        if (existPublicParameters.containsKey(id)) {
            return Optional.of(existPublicParameters.get(id));
        }

        return Optional.ofNullable(id).flatMap(pid -> parameterRefRepository.findById(pid).map(parameterRef -> {
            Parameter parameter = modelMapper.map(parameterRef, Parameter.class);
            parameter.setPublicItems(existPublicParameters);
            parameter.setPublicTypeDefinitions(existPublicTypeDefinitions);

            if (parameter.getScope().isPublic()){
                existPublicParameters.put(parameter.getId(),parameter);
            }


            // 填充类型定义
            typeDefinitionService.findById(parameterRef.getTypeDefinitionId(),existPublicTypeDefinitions)
                    .ifPresent(parameter::setType);

            // 填充值提取器
            valueProviderDefinitionService.findById(parameterRef.getValueProviderDefinitionId()
                    ,existPublicValueProviderDefinitions
                    ,existPublicParameters
                    ,existPublicTypeDefinitions
                    )
                    .ifPresent(parameter::setValue);
            // 填充默认值提取器
            valueProviderDefinitionService.findById(parameterRef.getDefaultValueProviderDefinitionId()
                            ,existPublicValueProviderDefinitions
                            ,existPublicParameters
                            ,existPublicTypeDefinitions
                    )
                    .ifPresent(parameter::setDefaultValue);

            // 填充子元素
            parameterItemRefRepository.findAll(
                            Example.of(
                                    ParameterItemRef.builder().belongParameterId(parameter.getId()).build()
                            )
                    )
                    .stream()
                    .sorted(Comparator.comparing(ParameterItemRef::getOrderIndex))
                    .map(itemRef -> {
                        // 填充子元素的类型定义
                        return findById(itemRef.getReferencedParameterId(),existPublicValueProviderDefinitions,existPublicParameters,existPublicTypeDefinitions).orElseThrow();
                    })
                    .forEach(parameter.getItems()::add);
            // 填充base
            Optional.ofNullable(baseService.findByKindAndBelongId("parameter", parameter.getId())).ifPresent(base -> {
                base.mergeInto(parameter);
            });
            return parameter;
        }));
    }


    @Override
    public Parameter save(Parameter parameter) {
        final ParameterRef parameterRef = modelMapper.map(parameter, ParameterRef.class);

        // 比较类型定义
        Optional.ofNullable(parameter.getType()).map(typeDefinition -> {
            // 保存类型定义
            if (Scope.isPublic(typeDefinition)){
                return typeDefinition;
            }
            return typeDefinitionService.save(typeDefinition);
        }).ifPresent(typeDefinition -> {
            if (!typeDefinition.getId().equals(parameterRef.getTypeDefinitionId())) {
                return;
            }
            parameterRef.setTypeDefinitionId(typeDefinition.getId());
            parameter.setType(typeDefinition);
        });

        // 比较值提取器
        Optional.ofNullable(parameter.getValue()).map(valueProviderDefinition -> {
            // 保存值提取器
            return valueProviderDefinitionService.save(valueProviderDefinition);
        }).ifPresent(valueProviderDefinition -> {
            if (!valueProviderDefinition.getId().equals(parameterRef.getValueProviderDefinitionId())) {
                return;
            }
            parameterRef.setValueProviderDefinitionId(valueProviderDefinition.getId());
            parameter.setValue(valueProviderDefinition);
        });

        // 比较默认值提取器
        Optional.ofNullable(parameter.getDefaultValue()).map(valueProviderDefinition -> {
            // 保存值提取器
            return valueProviderDefinitionService.save(valueProviderDefinition);
        }).ifPresent(valueProviderDefinition -> {
            if (!valueProviderDefinition.getId().equals(parameterRef.getDefaultValueProviderDefinitionId())) {
                return;
            }
            parameterRef.setDefaultValueProviderDefinitionId(valueProviderDefinition.getId());
            parameter.setDefaultValue(valueProviderDefinition);
        });

        ParameterRef savedParameterRef = Optional.ofNullable(parameter.getId())
                .flatMap(id -> parameterRefRepository.findById(id))
                .map(oldParameterRef -> {
                    if (oldParameterRef.equals(parameterRef)) {
                        return parameterRef;
                    }
                    return null;
                }).orElseGet(() -> parameterRefRepository.save(parameterRef));
        parameter.setId(savedParameterRef.getId());

        // 比较子元素
        List<ParameterItemRef> oldItemsRef = parameterItemRefRepository
                .findAll(Example.of(
                        ParameterItemRef
                                .builder()
                                .belongParameterId(savedParameterRef.getId())
                                .build()));


        // 保存子元素
        parameter.setItems(parameter.getSortedAllItems().stream().map(pi->{
            Parameter p = parameter.getMust(pi.getItemId());
            if (Scope.isPublic(p)){
                return p;
            }
            return this.save(p);
        }).toList());

        List<ParameterItemRef> itemRefs = parameter.getItems().stream().map(param -> {
            // 保存子元素引用
            ParameterItemRef itemRef = ParameterItemRef.builder()
                    .belongParameterId(savedParameterRef.getId())
                    .referencedParameterId(param.getId())
                    .orderIndex(parameter.getItems().indexOf(param))
                    .build();

            return parameterItemRefRepository.save(itemRef);
        }).toList();

        oldItemsRef.forEach(oldItemRef -> {
            if (itemRefs.stream().noneMatch(item -> item.getId().equals(oldItemRef.getId()))) {
                // 那些id一致的可以忽略掉,因为在前面已经被覆盖了
                // 不再持有对应的引用,删除,如果在前面被更新了,下面的语句通用不会执行删除操作
                parameterItemRefRepository.delete(oldItemRef);
                this.deleteById(oldItemRef.getReferencedParameterId(), 1, false);
            }
        });

        // 保存base
        Base base = baseService.save(modelMapper.map(parameter, Base.class), "parameter", parameter.getId());
        return base.mergeInto(parameter);
    }

    @Override
    public boolean delete(Parameter parameter, int stackCount, boolean includePublic) {
        if (stackCount > 0 && !includePublic) {
            if (parameter.getScope().equals(Scope.PUBLIC)) {
                return false;
            }
        }
        // 按顺序移除对应的数据
        // 移除值提取器
        Optional.ofNullable(parameter.getValue()).ifPresent(valueProviderDefinition -> {
            valueProviderDefinitionService.delete(valueProviderDefinition, stackCount + 1, includePublic);
        });
        // 移除默认值提取器
        Optional.ofNullable(parameter.getDefaultValue()).ifPresent(valueProviderDefinition -> {
            valueProviderDefinitionService.delete(valueProviderDefinition, stackCount + 1, includePublic);
        });
        // 移除子元素
        parameter.getItems().forEach(item -> {
            this.delete(item, stackCount + 1, includePublic);
        });
        // 移除base
        baseService.deleteByKindAndBelongId("parameter", parameter.getId());
        // 移除自身
        parameterRefRepository.deleteById(parameter.getId());
        return true;
    }


    public Optional<Parameter> createFromTypeDefinition(TypeDefinition parent, TypeDefinition typeDefinition) {
        // 遍历类型定义的结构
        return Optional.ofNullable(typeDefinition).map(td -> {
            // 创建Parameter实例
            return Parameter.builder()
                    .name(td.getName())
                    .describe(td.getDescribe())
                    .type(td)
                    .publicTypeDefinitions(typeDefinition.getType().getPublicItems())
                    .items(Optional.of(td.getType().getPrivateItems())
                            .map(itds -> itds.values()
                                    .stream()
                                    .map(itd -> createFromTypeDefinition(td, itd).orElse(null))
                                    .filter(Objects::nonNull)
                                    .toList()
                            )
                            .orElse(Collections.emptyList()))
                    .value(createValueProvider(parent, typeDefinition))
                    .build();
        });
    }

    public Parameter create(TypeDefinition td) {
        return create(td, new HashMap<>(), td.getType().getPublicItems());
    }

    public Parameter create(TypeDefinition td,Map<String,Parameter> existPublicParameters,Map<String,TypeDefinition> existPublicTypeDefinitions) {
        Parameter parameter = Parameter.builder()
                .name(td.getName())
                .describe(td.getDescribe())
                .type(td)
                .build();
        td.getAllItems().forEach((item)->{
            parameter.addItem(create(item,existPublicParameters,existPublicTypeDefinitions));
        });
        return parameter;
    }


    /**
     * 为参数创建值提供者,如果类型定义是一个基本类型,则创建一个默认值提供者
     * 如果类型定义所属的定义是一个复合类型,则创建一个父值提供者
     *
     * @param parent         父级参数定义
     * @param typeDefinition 类型定义
     * @return 值提供者
     */
    protected ValueProviderDefinition createValueProvider(TypeDefinition parent, TypeDefinition typeDefinition) {

        Optional<ValueProviderDefinition> par = Optional.ofNullable(parent)
                .map(p -> {
                    // 如果父级参数定义不为空,则创建一个父值提供者
                    // 如果父级参数定义是一个数组,那么当前定义只能是elements
                    // 理论上elements的作用是提供子元素类型定义
                    // 在这里elements的处理和其余的参数处理不一致,其本质上应该处理循环中的元素
                    // 所以隐式包含了一个循环数组的特性
                    // 但是这里不应该由ValueProvider去处理数组逻辑,所以应该交给谁呢?
                    // 解决方案: 交给上下文构建器去处理,为array单独构建一次循环上下文
                    // 所以就不需要单独配置array了,直接默认是用父元素提取器就可以刻

                    // 只要由父级结构,默认使用父元素提取器
                    return ValueProviderDefinition.builder()
                            .kind("parent")
                            .build();
                });

        // 如果父级参数定义为空,表示当前参数定义是一个根参数定义
        // 此时需要根据类型来决定创建什么样的值提供者
        // 比如:如果类型是一个基本类型,则创建一个默认值提供者
        // 如果类型是一个复合类型,则创建一个父值提供者
        return par.orElseGet(()-> ValueProviderDefinition.builder()
                .kind("sameName")
                .build());
    }
}
