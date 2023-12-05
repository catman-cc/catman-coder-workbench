package cc.catman.coder.workbench.core.parameter;

import java.util.Map;

/**
 * 参数解析处理器上下文
 */
public interface IParameterParseHandlerContext {
    Map<String,Object> buildVariables();
}
