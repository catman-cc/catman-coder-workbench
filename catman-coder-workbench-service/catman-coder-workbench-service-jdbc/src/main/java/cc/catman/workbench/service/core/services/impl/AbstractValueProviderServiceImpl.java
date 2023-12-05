package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.entity.Scope;
import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderConfig;
import cc.catman.workbench.service.core.po.ValueProviderConfigItemRef;
import cc.catman.workbench.service.core.po.ValueProviderConfigRef;
import cc.catman.workbench.service.core.po.ValueProviderRef;
import cc.catman.workbench.service.core.repossitory.IValueProviderConfigItemRefRepository;
import cc.catman.workbench.service.core.repossitory.IValueProviderConfigRefRepository;
import cc.catman.workbench.service.core.repossitory.IValueProviderRefRepository;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.services.IValueProviderService;
import cc.catman.workbench.service.core.services.IValueProviderServiceList;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 一个抽象的值提取服务定义,目的是为了简化实现新的值提取器的成本
 * @param <C> 值提取配置类型
 * @param <T> 值提取器类型
 */
public abstract class AbstractValueProviderServiceImpl<C extends ValueProviderConfig,T extends AbstractValueProvider<C>> implements IValueProviderService<C,T> {
    protected Class<T> providerClass;
    protected Class<C> configClass;
    @Resource
    protected ModelMapper modelMapper;
    @Resource
    protected IBaseService baseService;
    @Lazy
    @Resource
    protected IValueProviderServiceList providerServiceList;
    @Resource
    protected IValueProviderRefRepository valueProviderRefRepository;
    @Resource
    protected IValueProviderConfigRefRepository valueProviderConfigRefRepository;
    @Resource
    protected IValueProviderConfigItemRefRepository valueProviderConfigItemRefRepository;
    @Override
    public Optional<T> findById(String id) {
        // 获取值提取器
        return valueProviderRefRepository.findById(id).map(vpr->{
            T provider=modelMapper.map(vpr,providerClass);
            C cfg=createConfig();
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
                        items.forEach(item -> {
                           fillConfig(cfg,item);
                        });
                    });
            Optional.ofNullable(baseService.findBaseByBelongId(provider.getId())).ifPresent(base -> {
                base.mergeInto(provider);
            });
            return provider;
        });
    }

    @Override
    public T save(ValueProvider<? extends ValueProviderConfig> valueProvider) {
        if (valueProvider == null) {
            return null;
        }

        // 判断是否已经存在,如果存在的话,直接移除依赖
        Optional.ofNullable(valueProvider.getId()).flatMap(this::findById).ifPresent(this::delete);
        // 接下来就是标准的保存操作
        // 保存值提取器
        T provider= assertProvider(valueProvider).orElseThrow();

        ValueProviderRef providerRef=valueProviderRefRepository.save( modelMapper.map(provider, ValueProviderRef.class));

        // 保存基础数据
        Optional.ofNullable(baseService.save(modelMapper.map(provider, Base.class), "valueProvider", providerRef.getId())).ifPresent(base -> {
            base.mergeInto(provider);
        });

        // 保存值提取器配置
        // 保存值提取器配置项
        C config = provider.getConfig();

        ValueProviderConfigRef configRef = valueProviderConfigRefRepository
                .save(ValueProviderConfigRef
                        .builder()
                        .belongValueProviderId(providerRef.getId())
                        .belongValueProviderKind(providerRef.getKind())
                        .build()
                );
        config.setId(configRef.getId());

        valueProviderConfigItemRefRepository.saveAll(parseConfig(config).stream().filter(Objects::nonNull).toList());
        return provider;
    }


    @Override
    public boolean delete(T valueProvider,int stackCount, boolean includePublic) {
        if (stackCount>1&&!includePublic) {
            if (Scope.PRIVATE.equals(valueProvider.getScope())){
                return false;
            }
        }
        // 断言
        return this.assertProvider(valueProvider).map(ifp->{

            // 删除配置
            C config = ifp.getConfig();

            // 此处需要递归处理的是config子项依赖的数据
            deleteConfig(config,stackCount,includePublic);

            // 删除配置依赖关系
            valueProviderConfigItemRefRepository.delete(ValueProviderConfigItemRef.builder().valueProviderConfigId(config.getId()).build());
            // 删除配置项
            valueProviderConfigRefRepository.deleteById(config.getId());
            // 删除值提取器
            valueProviderRefRepository.deleteById(ifp.getId());
            return true;
        }).orElse(false);
    }

    /**
     * 断
     * @param valueProvider 值提取器
     * @return 断言结果
     */
    protected abstract Optional<T> assertProvider(ValueProvider<? extends ValueProviderConfig> valueProvider);

    /**
     * 创建配置对象
     */
    protected abstract C createConfig();

    /**
     * 创建值提取器
     */
    protected abstract T createProvider();

    /**
     * 填充配置信息
     * @param config 配置信息
     * @param item 配置项
     */
    protected abstract void fillConfig(C config, ValueProviderConfigItemRef item);

    /**
     * 解析配置信息
     * @param config 配置信息
     * @return 配置项
     */
    protected abstract List<ValueProviderConfigItemRef> parseConfig(C config);

    /**
     * 删除配置信息间接依赖的数据
     * @param config 配置信息
     * @param stackCount 栈计数器
     * @param includePublic 是否包含公共配置
     * @return 删除结果
     */
    protected abstract boolean deleteConfig(C config, int stackCount, boolean includePublic);
}
