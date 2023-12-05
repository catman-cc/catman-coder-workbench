package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderConfig;
import cc.catman.coder.workbench.core.value.ifs.IFValueProvider;
import cc.catman.coder.workbench.core.value.ifs.IFValueProviderConfig;
import cc.catman.workbench.service.core.po.ValueProviderConfigItemRef;
import cc.catman.workbench.service.core.po.ValueProviderConfigRef;
import cc.catman.workbench.service.core.po.ValueProviderRef;
import cc.catman.workbench.service.core.repossitory.IValueProviderConfigItemRefRepository;
import cc.catman.workbench.service.core.repossitory.IValueProviderConfigRefRepository;
import cc.catman.workbench.service.core.repossitory.IValueProviderRefRepository;
import cc.catman.workbench.service.core.services.IValueProviderService;
import cc.catman.workbench.service.core.services.IValueProviderServiceList;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class IfValueProviderServiceImpl implements IValueProviderService<IFValueProviderConfig,IFValueProvider> {
    @Resource
    private ModelMapper modelMapper;
    @Lazy
    @Resource
    private IValueProviderServiceList providerServiceList;
    @Resource
    private IValueProviderRefRepository valueProviderRefRepository;
    @Resource
    private IValueProviderConfigRefRepository valueProviderConfigRefRepository;
    @Resource
    private IValueProviderConfigItemRefRepository valueProviderConfigItemRefRepository;
    @Override
    public Optional<IFValueProvider> findById(String id) {
        // 获取值提取器
       return valueProviderRefRepository.findById(id).map(vpr->{
           IFValueProvider provider=modelMapper.map(vpr,IFValueProvider.class);
           IFValueProviderConfig cfg = new IFValueProviderConfig();
           provider.setConfig(cfg);
            // 填充配置信息
            valueProviderConfigRefRepository
                    .findOne(
                            Example.of(ValueProviderConfigRef
                                    .builder()
                                    .belongValueProviderId(provider.getId())
                                    .build()
                            )
                    )
                    .ifPresent(vpcr -> {
                        // 解析数据,构建配置信息
                        // 加载子配置信息
                        List<ValueProviderConfigItemRef> items = valueProviderConfigItemRefRepository.findAll(
                                Example.of(ValueProviderConfigItemRef
                                        .builder()
                                        .valueProviderConfigId(vpcr.getId())
                                        .build())
                        );
                        // 填充配置数据
                        // condition
                        // trueValueProvider
                        // falseValueProvider
                        items.forEach(item -> {
                            // item
                            switch (item.getFieldName()) {
                                case "condition" ->
                                        providerServiceList.findById(item.getReferencedValueProviderId()).ifPresent(cfg::setCondition);
                                case "trueValueProvider" ->
                                        providerServiceList.findById(item.getReferencedValueProviderId()).ifPresent(cfg::setT);
                                case "falseValueProvider" ->
                                       providerServiceList.findById(item.getReferencedValueProviderId()).ifPresent(cfg::setF);
                            }
                        });
                    });
            return provider;
        });
    }

    @Override
    public IFValueProvider save(ValueProvider<? extends ValueProviderConfig> valueProvider) {
        if (valueProvider == null) {
            return null;
        }

        // 判断是否已经存在,如果存在的话,直接移除依赖
        Optional.ofNullable(valueProvider.getId()).flatMap(this::findById).ifPresent(this::delete);
        // 接下来就是标准的保存操作
        // 保存值提取器
        IFValueProvider provider= assertProvider(valueProvider).orElseThrow();

        ValueProviderRef providerRef=valueProviderRefRepository.save( modelMapper.map(provider, ValueProviderRef.class));
        // 保存值提取器配置
        // 保存值提取器配置项
        IFValueProviderConfig config = provider.getConfig();
        ValueProviderConfigRef configRef = valueProviderConfigRefRepository
                .save(ValueProviderConfigRef
                        .builder()
                        .belongValueProviderId(providerRef.getId())
                        .belongValueProviderKind(providerRef.getKind())
                        .build()
                );
        config.setId(configRef.getId());

        valueProviderConfigItemRefRepository.saveAll(Stream.of(
                Optional.ofNullable(save(config.getCondition())).map(ValueProvider::getId).map(id ->
                        ValueProviderConfigItemRef
                                .builder()
                                .valueProviderConfigId(configRef.getId())
                                .fieldName("condition")
                                .referencedValueProviderId(id)
                                .build()
                ).orElse(null),
                Optional.ofNullable(save(config.getT())).map(ValueProvider::getId).map(id ->
                        ValueProviderConfigItemRef
                                .builder()
                                .valueProviderConfigId(configRef.getId())
                                .fieldName("t")
                                .referencedValueProviderId(id)
                                .build()
                ).orElse(null),
                Optional.ofNullable(save(config.getF())).map(ValueProvider::getId).map(id ->
                        ValueProviderConfigItemRef
                                .builder()
                                .valueProviderConfigId(configRef.getId())
                                .fieldName("f")
                                .referencedValueProviderId(id)
                                .build()
                ).orElse(null)
        ).filter(Objects::nonNull).toList());
        return provider;
    }


    @Override
    public boolean delete(IFValueProvider valueProvider,int stackCount,boolean includePublic) {
        // 断言
       return this.assertProvider(valueProvider).map(ifp->{
            // 删除配置
            IFValueProviderConfig config = ifp.getConfig();
            // 删除配置依赖关系
            providerServiceList.delete(config.getCondition(),stackCount+1,includePublic);
            providerServiceList.delete(config.getT(),stackCount+1,includePublic);
            providerServiceList.delete(config.getF(),stackCount+1,includePublic);
            valueProviderConfigItemRefRepository.delete(ValueProviderConfigItemRef.builder().valueProviderConfigId(config.getId()).build());
            // 删除配置项
            valueProviderConfigRefRepository.deleteById(config.getId());
            // 删除值提取器
            valueProviderRefRepository.deleteById(ifp.getId());
            return true;
        }).orElse(false);
    }

    protected Optional<IFValueProvider> assertProvider(ValueProvider<? extends ValueProviderConfig> valueProvider){
        return Optional.ofNullable(valueProvider).filter(v->v instanceof IFValueProvider).map(v->(IFValueProvider)v);
    }
}
