package cc.catman.coder.workbench.core.value.Switch;

import cc.catman.coder.workbench.core.value.DefaultValueProviderConfig;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderConfig;
import cc.catman.coder.workbench.core.value.ifs.IFValueProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 多条件判断取值配置信息
 * 本质上是对多个值提取器的聚合操作,并且可以指定每个值提取器的取值条件
 * 每一个条件都必须返回一个boolean值,如果为true,则使用该值提取器取值,否则不使用
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SwitchValueProviderConfig extends DefaultValueProviderConfig {
    private List<IFValueProvider> conditions;
    private ValueProvider<?> defaultProvider;
}
