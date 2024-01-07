package cc.catman.coder.workbench.core.value.providers.Switch;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import lombok.Builder;

import java.util.Optional;

/**
 * 多条件判断取值
 */
public class SwitchValueProvider extends AbstractValueProvider {

    @Builder.Default
    private String kind="switch";
    @Override
    public Optional<Object> run(ValueProviderContext context) {
     return  Optional.empty();
    }
}
