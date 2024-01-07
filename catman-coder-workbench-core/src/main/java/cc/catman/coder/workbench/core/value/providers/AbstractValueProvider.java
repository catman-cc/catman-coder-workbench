package cc.catman.coder.workbench.core.value.providers;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.value.ValueProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractValueProvider extends Base implements ValueProvider {
    private String id;
    private String name;
    private String kind;
    private String describe;
    private UniversalValueProviderConfig config;

    /**
     *  前置值提取器
     *  在处理时,将会优先处理,并使用其name作为变量名存储到上下文中
     */
    private List<ValueProvider> preValueProviders;

    /**
     *  后置值提取器
     */
    private List<ValueProvider> postValueProviders;

}
