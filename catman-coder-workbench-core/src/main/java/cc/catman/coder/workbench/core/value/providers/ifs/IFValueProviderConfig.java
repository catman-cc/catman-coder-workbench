package cc.catman.coder.workbench.core.value.providers.ifs;

import cc.catman.coder.workbench.core.value.providers.DefaultValueProviderConfig;
import cc.catman.coder.workbench.core.value.ValueProvider;
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

    private ValueProvider condition;
    /**
     * 满足condition时的值
     */
    private ValueProvider t;
    /**
     * 不满足condition时的值
     */
    private ValueProvider f;
}
