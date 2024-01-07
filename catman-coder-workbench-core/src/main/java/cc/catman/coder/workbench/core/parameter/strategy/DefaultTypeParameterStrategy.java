package cc.catman.coder.workbench.core.parameter.strategy;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.DefaultType;

public class DefaultTypeParameterStrategy implements IParameterParseStrategy {
    @Override
    public boolean support(Parameter parameter) {
        return DefaultType.class.equals(parameter.getType().getType().getClass());
    }

    @Override
    public Object parse(Parameter parameter, IParameterParseHandlerContext parameterParseHandlerContext) {
        return null;
    }
}
