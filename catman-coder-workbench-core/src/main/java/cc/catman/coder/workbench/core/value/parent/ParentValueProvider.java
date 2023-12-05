package cc.catman.coder.workbench.core.value.parent;

import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * 父级取值器
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ParentValueProvider extends AbstractValueProvider<ParentValueProviderConfig> {
    @Builder.Default
    private String kind="parent";
    @Override
    public Optional<Object> get(ValueProviderContext context) {
        return Optional.empty();
    }
}
