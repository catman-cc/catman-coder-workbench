package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;
/**
 * 匿名类型参数解析策略
 * 该策略和Map类型的参数解析策略基本一致,但是匿名类型参数,本身不支持取值器,因此在解析参数时,会忽略取值器
 * <p>
 * 同时匿名对象本身不持有上下文,因此在解析子参数时,会直接将子参数的值传递给父级上下文变量
 * 换句话说,匿名对象在解析时,会直接使用其所属的程序堆栈进行解析.
 */
public class AnonymousParameterParseStrategy extends AbstractParameterParseStrategy{
    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        return null;
    }

    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_ANONYMOUS;
    }
}
