package cc.catman.coder.workbench.core.value.providers;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultValueProvider extends AbstractValueProvider {
    @Override
    public Optional<Object> run(ValueProviderContext context) {
        return Optional.empty();
    }
}
