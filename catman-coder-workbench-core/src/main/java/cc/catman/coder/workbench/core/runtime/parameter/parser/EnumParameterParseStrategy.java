package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;


public class EnumParameterParseStrategy extends AbstractParameterParseStrategy{
    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        FunctionCallInfo functionCallInfo = getFunctionCallInfo(parameter);

        return functionCallInfo.call(stack);
        // 获取泛型值定义,并判断是否符合要求
//        TypeDefinition td = parameter.getType();
//        DefaultType type = td.getType();
//        // 所有支持的泛型定义
//        List<TypeDefinition> supportEnumItems = type.getAllItems();
////        supportEnumItems.stream().anyMatch(s->{
////           //TODO 检查对象是否符合要求
////        });
//        return null;
    }

    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_ENUM;
    }
}
