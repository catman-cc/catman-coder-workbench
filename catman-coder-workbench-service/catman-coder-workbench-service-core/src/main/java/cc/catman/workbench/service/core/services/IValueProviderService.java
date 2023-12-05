package cc.catman.workbench.service.core.services;


import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderConfig;

import java.util.Optional;

public interface IValueProviderService<C extends ValueProviderConfig,T extends ValueProvider<C>> {

    Optional<T> findById(String id);

    T save(ValueProvider<? extends ValueProviderConfig> valueProvider);

   default boolean deleteById(String id){
         return deleteById(id,false);
   }

   default boolean deleteById(String id,boolean includePublic){
       return this.findById(id).map(ifp->this.delete(ifp,includePublic)).orElse(false);
   }

   default boolean delete(T valueProvider){
       return delete(valueProvider,false);
   }

    default boolean delete(T valueProvider,boolean includePublic){
       return delete(valueProvider,0,includePublic);
    }

    boolean delete(T valueProvider,int stackCount,boolean includePublic);
}
