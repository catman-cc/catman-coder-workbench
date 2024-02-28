package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;

public abstract class AbstractParameterParseStrategy implements IParameterParseStrategy{
    @Override
    public boolean support(Parameter parameter) {
        TypeDefinition type = parameter.getType();
        String typeName = type.getType().getTypeName();
        return typeName.equals(getSupportTypeName());
    }

    @Override
    public Object parse(Parameter parameter, IRuntimeStack stack) {
        IFunctionCallInfo vf = parameter.getValueFunction();
        if (vf != null) {
            // 调用参数的值提取器,获取参数的值
            // Note: 此处获取的结果并不一定是最终对象,还需要根据参数类型以及参数的配置进行进一步的处理
            Object res = vf.call(stack);
           return this.doParse(parameter,res,stack);
        }
        return null;
    }

    /**
     * 解析参数
     * @param parameter 参数
     * @param preParseValue 预解析值
     * @param stack 堆栈
     * @return 解析后的值
     */
    public abstract Object doParse(Parameter parameter,Object preParseValue, IRuntimeStack stack);

    public abstract String getSupportTypeName();

}
