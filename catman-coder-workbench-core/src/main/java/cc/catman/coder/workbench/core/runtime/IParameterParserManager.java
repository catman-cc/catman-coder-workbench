package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.parameter.parser.IParameterParseStrategy;

/**
 * 参数解析器
 */
public interface IParameterParserManager {
    /**
     * 解析参数,根据参数定义,解析参数的值,并返回解析结果
     * Note: 经过parser方法处理得到的参数值,仍有可能包含一些内置数据类型,比如:Parameter,TypeDefinition等
     *       这取决于Parameter对应的TypeDefinition定义,比如:if函数的参数仍然允许接受FunctionCallInfo类型的参数
     *
     * @param parameter 参数
     * @param stack 运行时栈
     * @return 解析结果
     */
    ParameterParserResult parse(Parameter parameter, IRuntimeStack stack);

    /**
     * 注册参数解析策略
     * @param strategy 参数解析策略
     */
    void registerParameterParseStrategy(IParameterParseStrategy strategy);
}
