package cc.catman.coder.workbench.core.value.providers.combination;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;

import java.util.Optional;

/**
 * 组合 值提供者
 */
public class CombinationValueProvider extends AbstractValueProvider {
    @Override
    public Optional<Object> run(ValueProviderContext context) {
        return Optional.empty();
    }
}
