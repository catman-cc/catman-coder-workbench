package cc.catman.coder.workbench.core.value.ifs;

import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.Builder;

import java.util.Optional;

/**
 * 多条件判断取值配置信息
 */
public class IFValueProvider extends AbstractValueProvider<IFValueProviderConfig> {
    @Builder.Default
    private String kind="if";
    @Override
    public Optional<Object> get(ValueProviderContext context) {
        IFValueProviderConfig config = getConfig();
        if (config.getT().get(context,boolean.class,false)){
            return config.getT().get(context);
        }
        return Optional.empty();
    }

    @Override
    public <R> R get(ValueProviderContext context, Class<R> type, R defaultValue) {
        return super.get(context, type, defaultValue);
    }
}
