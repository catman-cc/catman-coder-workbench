package cc.catman.coder.workbench.core.value.ifs;

import cc.catman.coder.workbench.core.value.DefaultValueProviderConfig;
import cc.catman.coder.workbench.core.value.ValueProvider;
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
public class IFValueProviderConfig extends DefaultValueProviderConfig {
    private String id;

    private ValueProvider<? extends ValueProviderConfig> condition;
    /**
     * 满足condition时的值
     */
    private ValueProvider<? extends ValueProviderConfig> t;
    /**
     * 不满足condition时的值
     */
    private ValueProvider<? extends ValueProviderConfig> f;
}
