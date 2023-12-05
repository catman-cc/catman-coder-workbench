package cc.catman.coder.workbench.core.value;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultValueProvider extends AbstractValueProvider<ValueProviderConfig> {
    @Override
    public Optional<Object> get(ValueProviderContext context) {
        return Optional.empty();
    }
}
