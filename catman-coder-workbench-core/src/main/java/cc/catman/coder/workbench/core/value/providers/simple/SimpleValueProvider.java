package cc.catman.coder.workbench.core.value.providers.simple;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;


@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SimpleValueProvider extends AbstractValueProvider {
    public static final String KIND = "simple";
    @Builder.Default
    private String kind=KIND;

    private String value;
    @Override
    public Optional<Object> run(ValueProviderContext context) {
        return Optional.ofNullable(value);
    }
}
