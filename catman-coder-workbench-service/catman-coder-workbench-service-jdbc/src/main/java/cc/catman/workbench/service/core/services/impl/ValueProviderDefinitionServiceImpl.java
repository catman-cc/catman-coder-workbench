package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.common.Scope;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.po.ValueProviderParameterRef;
import cc.catman.workbench.service.core.po.ValueProviderDefinitionRef;
import cc.catman.workbench.service.core.po.ValueProviderUsageRef;
import cc.catman.workbench.service.core.repossitory.*;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.services.IParameterService;
import cc.catman.workbench.service.core.services.IValueProviderDefinitionService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 一个抽象的值提取服务定义,目的是为了简化实现新的值提取器的成本
 */
@Service
public class ValueProviderDefinitionServiceImpl implements IValueProviderDefinitionService {
    @Resource
    protected ModelMapper modelMapper;
    @Resource
    protected IBaseService baseService;

    @Resource
    protected IParameterService parameterService;
    @Resource
    protected IValueProviderDefinitionRefRepository valueProviderRefRepository;

    @Resource
    protected IValueProviderParameterRefRepository valueProviderParameterRefRepository;

    @Resource
    protected IValueProviderUsageRefRepository valueProviderUsageRefRepository;
    @Resource
    protected IValueProviderConfigRefRepository valueProviderConfigRefRepository;
    @Resource
    protected IValueProviderConfigItemRefRepository valueProviderConfigItemRefRepository;

    @Override
    public Optional<ValueProviderDefinition> findById(String id) {
        // 获取值提取器
        return valueProviderRefRepository.findById(id).map(vpr -> {
            // 获取值提取器定义
            ValueProviderDefinition provider = modelMapper.map(vpr, ValueProviderDefinition.class);

            // 获取参数定义
            valueProviderParameterRefRepository.findAll(
                    Example.of(ValueProviderParameterRef
                            .builder()
                            .belongValueProviderId(provider.getId())
                            .build()
                    )
            ).forEach(valueProviderParameterRef -> {
                // 获取参数
                parameterService.findById(valueProviderParameterRef.getParameterId()).ifPresent(parameter -> {
                    // 填充参数
                    if ("args".equals(valueProviderParameterRef.getFieldName())) {
                        provider.setArgs(parameter);
                    } else if ("result".equals(valueProviderParameterRef.getFieldName())) {
                        provider.setResult(parameter);
                    }
                });
            });

            valueProviderUsageRefRepository.findAll(Example.of(ValueProviderUsageRef
                            .builder()
                            .valueProviderId(provider.getId())
                            .build()))
                    .stream()
                    .collect(
                            Collectors.toMap(
                                    ValueProviderUsageRef::getKind
                                    , Collections::singletonList
                                    , (v1, v2) -> Stream.concat(v1.stream(), v2.stream()).sorted(Comparator.comparingInt(ValueProviderUsageRef::getOrderIndex))
                                            .collect(Collectors.toList())
                            )
                    )
                    .forEach((kind, list) -> {
                        List<ValueProviderDefinition> vpds = list.stream()
                                .map(ValueProviderUsageRef::getReferencedValueProviderId)
                                .map(this::findById)
                                .map(Optional::orElseThrow)
                                .toList();
                        switch (kind){
                            case "preValueProviders" -> provider.setPreValueProviders(vpds);
                            case "postValueProviders" -> provider.setPostValueProviders(vpds);
                        }
                    });
            Optional.ofNullable(baseService.findByKindAndBelongId("valueProvider",provider.getId())).ifPresent(base -> {
                base.mergeInto(provider);
            });
            return provider;
        });
    }

    @Override
    public ValueProviderDefinition save(ValueProviderDefinition valueProvider) {
        if (valueProvider == null) {
            return null;
        }

        // 判断是否已经存在,如果存在的话,直接移除依赖
        Optional.ofNullable(valueProvider.getId()).flatMap(this::findById).ifPresent(this::delete);


        // 接下来就是标准的保存操作
        // 保存值提取器主体定义
        ValueProviderDefinitionRef providerRef = valueProviderRefRepository.save(modelMapper.map(valueProvider, ValueProviderDefinitionRef.class));
        modelMapper.map(providerRef, valueProvider);

        Optional.ofNullable(valueProvider.getArgs()).ifPresent(args->{
            Parameter saved = parameterService.save(args);
            valueProvider.setArgs(saved);
            valueProviderParameterRefRepository.save(ValueProviderParameterRef
                    .builder()
                    .belongValueProviderId(providerRef.getId())
                    .belongValueProviderKind(providerRef.getKind())
                    .parameterId(saved.getId())
                    .fieldName("args")
                    .build()
            );
        });

        Optional.ofNullable(valueProvider.getResult()).ifPresent(result->{
            Parameter saved = parameterService.save(result);
            valueProvider.setResult(saved);
            valueProviderParameterRefRepository.save(ValueProviderParameterRef
                    .builder()
                    .belongValueProviderId(providerRef.getId())
                    .belongValueProviderKind(providerRef.getKind())
                    .parameterId(saved.getId())
                    .fieldName("result")
                    .build()
            );
        });

        // 保存值提取器的依赖关系
        Optional.ofNullable(valueProvider.getPreValueProviders()).ifPresent(preValueProviders -> {
            for (var ref = new Object() {
                int i = 0;
            }; ref.i < preValueProviders.size(); ref.i++) {
                ValueProviderDefinition provider = preValueProviders.get(ref.i);
                ValueProviderDefinitionRef pre = valueProviderRefRepository.save(modelMapper.map(provider, ValueProviderDefinitionRef.class));
                valueProviderUsageRefRepository.save(ValueProviderUsageRef
                        .builder()
                        .valueProviderId(providerRef.getId())
                        .kind(valueProvider.getKind())
                        .referencedValueProviderId(pre.getId())
                        .kind("preValueProviders")
                        .orderIndex(ref.i)
                        .build()
                );
            }
        });

        Optional.ofNullable(valueProvider.getPostValueProviders()).ifPresent(postValueProviders -> {
            for (var ref = new Object() {
                int i = 0;
            }; ref.i < postValueProviders.size(); ref.i++) {
                ValueProviderDefinition provider = postValueProviders.get(ref.i);
                ValueProviderDefinitionRef post = valueProviderRefRepository.save(modelMapper.map(provider, ValueProviderDefinitionRef.class));
                valueProviderUsageRefRepository.save(ValueProviderUsageRef
                        .builder()
                        .valueProviderId(providerRef.getId())
                        .kind(valueProvider.getKind())
                        .referencedValueProviderId(post.getId())
                        .kind("postValueProviders")
                        .orderIndex(ref.i)
                        .build()
                );
            }
        });

        // 保存基础数据
        Optional.ofNullable(baseService.save(modelMapper.map(valueProvider, Base.class), "valueProvider", providerRef.getId())).ifPresent(base -> {
            base.mergeInto(valueProvider);
        });

        return valueProvider;
    }


    @Override
    public boolean delete(ValueProviderDefinition valueProvider, int stackCount, boolean includePublic) {
        if (stackCount > 1 && !includePublic) {
            if (Scope.PUBLIC.equals(valueProvider.getScope())) {
                return false;
            }
        }
        // 删除级联的任务定义
        valueProviderUsageRefRepository.deleteAll(
                valueProviderUsageRefRepository.findAll(
                        Example.of(
                                ValueProviderUsageRef
                                        .builder()
                                        .valueProviderId(valueProvider.getId())
//                                                    .referencedValueProviderId(provider.getId())
//                                        .kind("preValueProviders")
                                        .build()
                        )
                )
        );
        Optional.ofNullable(valueProvider.getPreValueProviders()).ifPresent(preValueProviders -> {
            preValueProviders.forEach(provider -> {
                delete(provider, stackCount + 1, includePublic);
            });
        });

        Optional.ofNullable(valueProvider.getPostValueProviders()).ifPresent(postValueProviders -> {
            postValueProviders.forEach(provider -> {
                delete(provider, stackCount + 1, includePublic);
            });
        });
        // 删除参数定义,TODO 删除定义有风险,因为可能有其他的值提取器依赖于此参数,所以这里后续需要增加一个判断
        valueProviderParameterRefRepository.findAll(
                Example.of(
                        ValueProviderParameterRef
                                .builder()
                                .belongValueProviderId(valueProvider.getId())
                                .build()
                )
        );
        Optional.ofNullable(valueProvider.getArgs()).ifPresent(args -> {
            parameterService.deleteById(args.getId());
        });
        Optional.ofNullable(valueProvider.getResult()).ifPresent(result -> {
            parameterService.deleteById(result.getId());
        });
        // 删除基础数据
        baseService.deleteByKindAndBelongId("valueProvider", valueProvider.getId());
        // 断言
        return true;
    }
}
