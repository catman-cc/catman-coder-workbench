package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractValueProvider<T extends ValueProviderConfig> extends Base implements ValueProvider<T> {
    private String id;
    private String name;
    private String kind;
    private String describe;
    private T config;
}
