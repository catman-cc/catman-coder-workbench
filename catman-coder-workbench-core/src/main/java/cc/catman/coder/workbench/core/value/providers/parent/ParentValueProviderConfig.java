package cc.catman.coder.workbench.core.value.providers.parent;

import cc.catman.coder.workbench.core.value.providers.DefaultValueProviderConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ParentValueProviderConfig extends DefaultValueProviderConfig {
    /**
     * 该数据默认从父级上下文读取数据,如果name为空,则使用Parameter的name参数作为name
     */
    private String name;
}
