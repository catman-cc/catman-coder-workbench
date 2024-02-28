package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.Parameter;

public class StringParameterParseStrategy extends AbstractParameterParseStrategy{

    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_STRING;
    }

    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        if(preParseValue instanceof String){
            return preParseValue;
        }
        return stack.convertTo(preParseValue, String.class);
    }
}
