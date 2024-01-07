package cc.catman.coder.workbench.core.value.providers.ifs;

import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IFValueProviderArgs {

    @Builder.Default
    private List<ConditionPair> conditionPairs=new ArrayList<>();
    private ValueProviderDefinition defaultValue;
}
