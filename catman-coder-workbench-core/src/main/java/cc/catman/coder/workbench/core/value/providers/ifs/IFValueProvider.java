package cc.catman.coder.workbench.core.value.providers.ifs;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * 多条件判断取值配置信息
 * 其入参包含了一个条件组,以及一个默认值
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IFValueProvider extends AbstractValueProvider {
    @Builder.Default
    private String kind = "if";

    private IFValueProviderArgs args;

    @Override
    public Optional<Object> run(ValueProviderContext context) {
        // 遍历条件组,如果满足条件,则返回对应的值
        return Optional.ofNullable(args.getConditionPairs()
                .stream()
                .filter(conditionPair -> context.exec(conditionPair.getCondition(),boolean.class))
                .findFirst().map(conditionPair -> context.exec(conditionPair.getValue(),Object.class))
                .orElseGet(() -> Optional.ofNullable(args.getDefaultValue())
                        .map(valueProviderDefinition -> context.exec(valueProviderDefinition, Object.class))
                        .orElse(null)));
    }

    @Override
    public <R> R run(ValueProviderContext context, Class<R> type, R defaultValue) {
        return super.run(context, type, defaultValue);
    }
}
