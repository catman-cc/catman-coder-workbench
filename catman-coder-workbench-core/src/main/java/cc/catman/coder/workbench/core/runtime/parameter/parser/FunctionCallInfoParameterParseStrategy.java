package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;

import java.util.Optional;

/**
 * 函数调用信息参数解析策略
 * 函数调用信息解析中包含一个隐式逻辑:
 * - FunctionCallInfo是否需要进行预准备工作,即,其依赖的参数的解析堆栈在什么时候准备.
 * 目前我的实现是在解析时准备,而不是在执行时准备,因为执行时准备可能会访问到一些预期外的变量值.
 * 坏处就是,可能会执行一些无用的准备工作,但是这个开销是可以接受的.
 */
public class FunctionCallInfoParameterParseStrategy extends AbstractParameterParseStrategy {

    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        return getFunctionCallInfo(parameter);
    }

    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_FUNCTION_CALL_INFO;
    }
}
