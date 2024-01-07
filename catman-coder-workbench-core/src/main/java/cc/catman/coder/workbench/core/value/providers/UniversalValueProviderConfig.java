package cc.catman.coder.workbench.core.value.providers;

import cc.catman.coder.workbench.core.parameter.Parameter;
import lombok.Data;

/**
 * 通用的值提供者配置
 */
@Data
public class UniversalValueProviderConfig {

    private Parameter args;

    private Parameter result;
}
