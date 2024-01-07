package cc.catman.coder.workbench.core.value.providers.expressions;

import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProviderFactory;

public class ExpressionsValueProviderFactory extends AbstractValueProviderFactory {
    @Override
    protected AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry, ValueProviderContext context) {
        return ExpressionsValueProvider.builder()
                .language(context.getVariable("language", String.class))
                .expression(context.getVariable("expression", String.class))
                .build();
    }
}
