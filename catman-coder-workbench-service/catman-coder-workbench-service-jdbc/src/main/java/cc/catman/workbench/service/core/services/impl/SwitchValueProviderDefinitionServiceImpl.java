//package cc.catman.workbench.service.core.services.impl;
//
//import cc.catman.coder.workbench.core.value.providers.Switch.SwitchValueProvider;
//import cc.catman.coder.workbench.core.value.providers.Switch.SwitchValueProviderConfig;
//import cc.catman.coder.workbench.core.value.ValueProvider;
//import cc.catman.coder.workbench.core.value.ValueProviderConfig;
//import cc.catman.coder.workbench.core.value.providers.ifs.IFValueProvider;
//import cc.catman.workbench.service.core.po.valueProvider.ValueProviderConfigItemRef;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Switch值提取器
// */
//@Service
//public class SwitchValueProviderDefinitionServiceImpl extends ValueProviderDefinitionServiceImpl<SwitchValueProviderConfig, SwitchValueProvider> {
//
//    @Override
//    protected Optional<SwitchValueProvider> assertProvider(ValueProvider<? extends ValueProviderConfig> valueProvider) {
//        return Optional.empty();
//    }
//
//    @Override
//    protected SwitchValueProviderConfig createConfig() {
//        return new SwitchValueProviderConfig();
//    }
//
//    @Override
//    protected SwitchValueProvider createProvider() {
//        return new SwitchValueProvider();
//    }
//
//    @Override
//    protected void fillConfig(SwitchValueProviderConfig config, ValueProviderConfigItemRef item) {
//        if (item.isFiled("conditions")) {
//            if (CollectionUtils.isEmpty(config.getConditions())) {
//                config.setConditions(new ArrayList<>());
//            }
//            IFValueProvider provider = (IFValueProvider) providerServiceList.findById(item.getReferencedValueProviderId())
//                    .orElseThrow(() -> new RuntimeException("未找到值提取器:" + item.getReferencedValueProviderId()));
//            config.getConditions().add(item.getOrderIndex(),provider);
//        } else if (item.isFiled("defaultProvider")) {
//            config.setDefaultProvider(providerServiceList.findById(item.getReferencedValueProviderId())
//                    .orElseThrow(() -> new RuntimeException("未找到值提取器:" + item.getReferencedValueProviderId())));
//        }
//    }
//
//    @Override
//    protected List<ValueProviderConfigItemRef> parseConfig(SwitchValueProviderConfig config) {
//        List<ValueProviderConfigItemRef> items = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(config.getConditions())) {
//            for (var ref = new Object() {
//                int i = 0;
//            }; ref.i < config.getConditions().size(); ref.i++) {
//                Optional.ofNullable(save(config.getConditions().get(ref.i)))
//                        .ifPresent(provider->{
//                            ValueProviderConfigItemRef item = new ValueProviderConfigItemRef();
//                            item.setOrderIndex(ref.i);
//                            item.setReferencedValueProviderId(provider.getId());
//                            item.setFieldName("conditions");
//                            items.add(ValueProviderConfigItemRef.builder()
//                                    .orderIndex(ref.i)
//                                    .referencedValueProviderId(provider.getId())
//                                    .fieldName("conditions")
//                                    .valueProviderConfigId(config.getId())
//                                    .build());
//                        });
//
//
//            }
//        }
//        Optional.ofNullable(save(config.getDefaultProvider())).ifPresent(provider->{
//            ValueProviderConfigItemRef item = new ValueProviderConfigItemRef();
//            item.setOrderIndex(0);
//            item.setReferencedValueProviderId(provider.getId());
//            item.setFieldName("defaultProvider");
//            items.add(ValueProviderConfigItemRef.builder()
//                    .orderIndex(0)
//                    .referencedValueProviderId(provider.getId())
//                    .fieldName("defaultProvider")
//                    .valueProviderConfigId(config.getId())
//                    .build());
//        });
//        return items;
//    }
//
//    @Override
//    protected boolean deleteConfig(SwitchValueProviderConfig config, int stackCount,boolean includePublic) {
//        Optional.ofNullable(config.getConditions()).ifPresent(conditions->{
//            conditions.forEach(condition->{
//               providerServiceList.delete(condition,stackCount+1,includePublic);
//            });
//        });
//        Optional.ofNullable(config.getDefaultProvider()).ifPresent(provider->{
//            providerServiceList.delete(provider,stackCount+1,includePublic);
//        });
//        return true;
//    }
//}
