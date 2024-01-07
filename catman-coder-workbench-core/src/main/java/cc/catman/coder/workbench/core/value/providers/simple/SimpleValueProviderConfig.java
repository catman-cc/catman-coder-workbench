package cc.catman.coder.workbench.core.value.providers.simple;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.providers.DefaultValueProviderConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SimpleValueProviderConfig extends DefaultValueProviderConfig {


    private Object value;

    private Parameter targetParameter;

}
