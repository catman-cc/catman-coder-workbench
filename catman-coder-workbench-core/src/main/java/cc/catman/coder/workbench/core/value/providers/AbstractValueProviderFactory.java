package cc.catman.coder.workbench.core.value.providers;

import cc.catman.coder.workbench.core.value.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public abstract class AbstractValueProviderFactory implements ValueProviderFactory {
    @Override
    public ValueProvider createValueProvider(ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry, ValueProviderContext context) {
        return fill(create(valueProviderDefinition,valueProviderRegistry,context),valueProviderDefinition,valueProviderRegistry,context);
    }
    protected abstract AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition
            , ValueProviderRegistry valueProviderRegistry
            ,ValueProviderContext context
    );

   protected  <T extends AbstractValueProvider> T fill(T valueProvider, ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry,ValueProviderContext context) {
        valueProvider.setId(valueProviderDefinition.getId());
        valueProvider.setName(valueProviderDefinition.getName());
        valueProvider.setKind(valueProviderDefinition.getKind());
        valueProvider.setDescribe(valueProviderDefinition.getDescribe());
        valueProvider.setPreValueProviders(valueProviderDefinition.getPreValueProviders().stream().map(pvp->valueProviderRegistry.parse(pvp,context)).toList());
        valueProvider.setPostValueProviders(valueProviderDefinition.getPostValueProviders().stream().map(pvp->valueProviderRegistry.parse(pvp,context)).toList());
        return valueProvider;
    }
}
