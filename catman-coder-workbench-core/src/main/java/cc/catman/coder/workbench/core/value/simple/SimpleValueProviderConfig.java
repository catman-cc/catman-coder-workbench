package cc.catman.coder.workbench.core.value.simple;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.DefaultValueProviderConfig;
import lombok.AllArgsConstructor;
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
