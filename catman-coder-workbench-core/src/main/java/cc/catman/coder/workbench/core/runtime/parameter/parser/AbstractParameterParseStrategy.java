package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.parameter.parser.IParameterParseStrategy;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public abstract class AbstractParameterParseStrategy implements IParameterParseStrategy {
    @Override
    public boolean support(Parameter parameter) {
        TypeDefinition type = parameter.getType();
        String typeName = type.getType().getTypeName();
        return typeName.equals(getSupportTypeName());
    }

    @Override
    public Object parse(Parameter parameter, IRuntimeStack stack) {
        // 获取参数的值提取器
        IFunctionCallInfo vf = Optional.ofNullable(parameter.getValueFunction())
                .orElse(parameter.getDefaultValueFunction());

        // 调用参数的值提取器,获取参数的值
        // Note: 此处获取的结果并不一定是最终对象,还需要根据参数类型以及参数的配置进行进一步的处理
        Object res = vf == null ? null : vf.call(stack);
        // 此处根据参数的名称填充stack变量表
        log.debug("set variable: {} = {}", parameter.getName(), res);
        Optional.ofNullable(parameter.getName())
                .ifPresent(name -> stack.getVariablesTable()
                        .setVariable(parameter.getName(), res));

        return Optional.ofNullable(res)
                .map(preParseValue -> doParse(parameter, preParseValue, stack))
                .orElseGet(() -> handlerNullValue(parameter, stack));
    }
    /**
     * 处理参数值为null的情况
     * @param parameter 参数
     * @param stack 运行时栈
     * @return 处理后的值
     */
    protected  Object handlerNullValue(Parameter parameter, IRuntimeStack stack){
        return null;
    }

    /**
     * 解析参数
     *
     * @param parameter     参数
     * @param preParseValue 预解析值
     * @param stack         堆栈
     * @return 解析后的值
     */
    public abstract Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack);

    public abstract String getSupportTypeName();

}
