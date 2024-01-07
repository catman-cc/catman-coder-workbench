package cc.catman.coder.workbench.core.value.providers.simple;

import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProviderFactory;

public class SimpleValueProviderFactory extends AbstractValueProviderFactory {
    @Override
    protected AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry, ValueProviderContext context) {
        return SimpleValueProvider.builder()
                .value(valueProviderDefinition.getConfig())
                .build();
    }
}
