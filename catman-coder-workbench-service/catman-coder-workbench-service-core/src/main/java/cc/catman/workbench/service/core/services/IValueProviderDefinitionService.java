package cc.catman.workbench.service.core.services;


import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.ILoopReferenceContext;

import java.util.Map;
import java.util.Optional;

public interface IValueProviderDefinitionService {

    Optional<ValueProviderDefinition> findById(String id);
    Optional<ValueProviderDefinition> findById(String id, ILoopReferenceContext context);

    ValueProviderDefinition save(ValueProviderDefinition valueProviderDefinition);

    default boolean deleteById(String id) {
        return deleteById(id, false);
    }

    default boolean deleteById(String id, boolean includePublic) {
        return this.findById(id).map(ifp -> this.delete(ifp, includePublic)).orElse(false);
    }

    default boolean delete(ValueProviderDefinition valueProvider) {
        return delete(valueProvider, false);
    }

    default boolean delete(ValueProviderDefinition valueProvider, boolean includePublic) {
        return delete(valueProvider, 0, includePublic);
    }

    boolean delete(ValueProviderDefinition valueProvider, int stackCount, boolean includePublic);
}
