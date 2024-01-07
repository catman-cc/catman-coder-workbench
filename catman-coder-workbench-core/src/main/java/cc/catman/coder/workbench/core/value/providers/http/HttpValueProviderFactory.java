package cc.catman.coder.workbench.core.value.providers.http;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProviderFactory;

public class HttpValueProviderFactory extends AbstractValueProviderFactory {
    @Override
    protected AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry, ValueProviderContext context) {
        Parameter argParam = valueProviderDefinition.getArgs();
        HttpValueProviderArgs args = context.parse(argParam, HttpValueProviderArgs.class);

        return HttpValueProvider.builder()
                .args(args)
                .build()
                ;
    }
}
