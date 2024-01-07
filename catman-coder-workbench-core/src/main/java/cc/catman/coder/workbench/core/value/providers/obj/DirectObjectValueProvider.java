package cc.catman.coder.workbench.core.value.providers.obj;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;

import java.util.Optional;

/**
 *  直接对象取值器,其接收一个对象,并将其作为值返回
 */
public class DirectObjectValueProvider extends AbstractValueProvider {
    private Object value;
    @Override
    public Optional<Object> run(ValueProviderContext context) {
        return Optional.ofNullable(value);
    }
}
