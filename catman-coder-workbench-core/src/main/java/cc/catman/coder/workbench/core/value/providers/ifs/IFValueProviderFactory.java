package cc.catman.coder.workbench.core.value.providers.ifs;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProviderFactory;

public class IFValueProviderFactory extends AbstractValueProviderFactory {
    @Override
    protected AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry, ValueProviderContext context) {
        Parameter argsParam = valueProviderDefinition.getArgs();
        // 解析参数对象
        IFValueProviderArgs args = IFValueProviderArgs.builder().build();

        // 获取所提供的参数定义
        argsParam.get("conditionPairs").ifPresent(parameter -> {
            parameter.getItems().forEach(item -> {
                // 获取参数定义的值
                Parameter condition = item.get("condition").orElseThrow();
                Parameter value = item.get("value").orElseThrow();
                args.getConditionPairs().add(ConditionPair.builder().condition(condition.getValue()).value(value.getValue()).build());
            });
        });

        argsParam.get("defaultValue").ifPresent(parameter -> {
            args.setDefaultValue(parameter.getValue());
        });

        return fill(IFValueProvider.builder()
                .args(args)
                .build(), valueProviderDefinition, valueProviderRegistry,context);
    }
}
