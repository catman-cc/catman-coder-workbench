package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.type.DefaultType;

public class DefaultParameterTypeParser implements IParameterTypeParser{
    @Override
    public Object parse(Parameter parameter) {
        // 判断是原始类型
        DefaultType type = parameter.getType().getType();
        if(type.isComplex()){
            // 复合类型,解析是数组或者是其他对象
        }
        return null;
    }
}
