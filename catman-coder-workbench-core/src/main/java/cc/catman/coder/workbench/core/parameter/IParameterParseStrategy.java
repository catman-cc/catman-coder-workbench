package cc.catman.coder.workbench.core.parameter;

/**
 * 参数解析策略
 */
public interface IParameterParseStrategy {
    boolean support(Parameter parameter);

    Object parse(Parameter parameter,IParameterParseHandlerContext parameterParseHandlerContext);
}
