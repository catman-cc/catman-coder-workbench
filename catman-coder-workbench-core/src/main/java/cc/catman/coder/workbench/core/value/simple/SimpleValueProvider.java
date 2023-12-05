package cc.catman.coder.workbench.core.value.simple;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;


@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SimpleValueProvider extends AbstractValueProvider<SimpleValueProviderConfig> {
    public static final String KIND = "simple";
    @Builder.Default
    private String kind=KIND;
    @Override
    public Optional<Object> get(ValueProviderContext context) {
        return Optional.ofNullable(getConfig().getValue());
    }
}
