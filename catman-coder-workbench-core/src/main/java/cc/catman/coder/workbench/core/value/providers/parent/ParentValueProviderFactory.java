package cc.catman.coder.workbench.core.value.providers.parent;

import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProviderFactory;
import cc.catman.coder.workbench.core.value.providers.expressions.ExpressionsValueProvider;

import java.util.Optional;

public class ParentValueProviderFactory extends AbstractValueProviderFactory {
    @Override
    protected AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition
            , ValueProviderRegistry valueProviderRegistry
            , ValueProviderContext context
    ) {

//        return ParentValueProvider.builder()
//                .name(context.getVariable("name", String.class))
//                .build();
        String name = Optional.ofNullable(context.parse(valueProviderDefinition.getArgs(), String.class))
                .orElseGet(() -> context.getVariable("__parameter__name__", String.class));

        return ExpressionsValueProvider.builder()
                .expression(name)
                .language("spel")
                .build();
    }
}
