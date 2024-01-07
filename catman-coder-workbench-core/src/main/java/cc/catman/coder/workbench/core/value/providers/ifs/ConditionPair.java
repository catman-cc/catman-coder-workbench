package cc.catman.coder.workbench.core.value.providers.ifs;

import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConditionPair {
    private ValueProviderDefinition condition;
    private ValueProviderDefinition value;
}
