package cc.catman.coder.workbench.core.value.expressions;

import cc.catman.coder.workbench.core.value.DefaultValueProviderConfig;
import cc.catman.coder.workbench.core.value.ValueProviderConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ExpressionsValueProviderConfig extends DefaultValueProviderConfig {
    private  String expression;
    private  String language;
}
