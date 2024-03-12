package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;

/**
 * 参数解析策略,负责完成参数的解析工作
 */
public interface IParameterParseStrategy {

    /**
     * 是否支持此参数的解析
     * @param parameter 参数
     * @return 是否支持
     */
    boolean support(Parameter parameter);

    /**
     * 解析参数,并获取解析结果
     * @param parameter 参数
     * @param stack 运行时栈
     * @return 解析结果
     */
    Object parse(Parameter parameter,IRuntimeStack stack);
}
