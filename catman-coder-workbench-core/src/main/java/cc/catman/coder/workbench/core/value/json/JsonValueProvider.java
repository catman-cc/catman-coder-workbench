package cc.catman.coder.workbench.core.value.json;

import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * JSON取值器
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JsonValueProvider extends AbstractValueProvider<JsonValueProviderConfig> {
    @Builder.Default
    private String kind="json";

    @Override
    public Optional<Object> get(ValueProviderContext context) {
        // TODO
        return Optional.empty();
    }
}
