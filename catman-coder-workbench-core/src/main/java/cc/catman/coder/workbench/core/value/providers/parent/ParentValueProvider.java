package cc.catman.coder.workbench.core.value.providers.parent;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.PropertyAccessor;

import java.util.Optional;

/**
 * 父级取值器
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ParentValueProvider extends AbstractValueProvider {
    @Builder.Default
    private String kind="parent";

    private String value;

    @Override
    public Optional<Object> run(ValueProviderContext context) {
        PropertyAccessor accessor = context.createPropertyAccessor(context.buildVariables());
        return Optional.ofNullable(accessor.getPropertyValue("parent."+value));
    }
}
