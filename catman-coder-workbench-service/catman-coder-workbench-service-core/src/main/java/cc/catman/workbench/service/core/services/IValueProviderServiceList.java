//package cc.catman.workbench.service.core.services;
//
//
//import cc.catman.coder.workbench.core.value.ValueProvider;
//import cc.catman.coder.workbench.core.value.ValueProviderConfig;
//
//import java.util.Optional;
//
///**
// * 持有所有的ValueProviderService
// */
//public interface IValueProviderServiceList {
//
//    Optional<ValueProvider<? extends ValueProviderConfig>> findById(String id);
//
//    IValueProviderDefinitionService getValueProviderService(ValueProvider valueProvider);
//
//    IValueProviderDefinitionService getValueProviderService(String kind);
//
//    ValueProvider<? extends ValueProviderConfig> save(ValueProvider<? extends ValueProviderConfig> valueProvider);
//    default boolean delete(ValueProvider<? extends ValueProviderConfig> valueProvider){
//        return delete(valueProvider,false);
//    }
//
//  default   boolean delete(ValueProvider<? extends ValueProviderConfig> valueProvider,boolean includePublic){
//        return delete(valueProvider,0,includePublic);
//  }
//
//    boolean delete(ValueProvider<? extends ValueProviderConfig> valueProvider,int stackCount,boolean includePublic);
//}
