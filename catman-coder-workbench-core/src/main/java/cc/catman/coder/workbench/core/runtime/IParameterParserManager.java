package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;

/**
 * 参数解析器
 */
public interface IParameterParserManager {
    /**
     * 解析参数
     * @param parameter 参数
     * @param stack 运行时栈
     * @return 解析结果
     */
    ParameterParserResult parse(Parameter parameter, IRuntimeStack stack);
}
