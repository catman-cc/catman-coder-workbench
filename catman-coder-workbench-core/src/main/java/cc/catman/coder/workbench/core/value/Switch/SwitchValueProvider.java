package cc.catman.coder.workbench.core.value.Switch;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ifs.IFValueProvider;
import lombok.Builder;

import java.util.Optional;

/**
 * 多条件判断取值
 */
public class SwitchValueProvider extends AbstractValueProvider<SwitchValueProviderConfig> {

    @Builder.Default
    private String kind="switch";
    @Override
    public Optional<Object> get(ValueProviderContext context) {
        SwitchValueProviderConfig config = getConfig();
        for (IFValueProvider ifValueProvider : config.getConditions()) {
            if (ifValueProvider.get(context,boolean.class,false)){
                return ifValueProvider.get(context);
            }
        }
        return Optional.ofNullable(config.getDefaultProvider()).map(valueProvider -> valueProvider.get(context));
    }
}
