package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.value.providers.ifs.ConditionPair;
import cc.catman.coder.workbench.core.value.providers.ifs.IFValueProviderArgs;

import java.util.List;
import java.util.Map;

public class IfFunctionExecutor extends AbstractFunctionExecutor {


    @Override
    protected Object doParseArgs(IFunctionCallInfo callInfo, IRuntimeStack stack, Map<String, Parameter> args, Map<String, Object> argsValues) {
        if (argsValues instanceof IFValueProviderArgs) {
            return argsValues;
        }
        // 主动获取对象值,进行解析
        Object cps = argsValues.get("conditionPairs");
        IFValueProviderArgs ifValueProviderArgs = new IFValueProviderArgs();
        List<ConditionPair> ls = (cps instanceof List<?>
                ? (List<?>) cps
                : stack.convertTo(cps, List.class)
        )
                .stream().map(o -> {
                    if (o instanceof ConditionPair cp) {
                        return cp;
                    }
                    return stack.convertTo(o, ConditionPair.class);
                }).toList();
        ifValueProviderArgs.setConditionPairs(ls);

        if (argsValues.containsKey("defaultValue")) {
            Object dv = argsValues.get("defaultValue");
            ifValueProviderArgs.setDefaultValue(dv instanceof ValueProviderDefinition
                    ? (ValueProviderDefinition) dv
                    : stack.convertTo(dv, ValueProviderDefinition.class)
            );

        }
        return ifValueProviderArgs;
    }

    @Override
    protected Object doExecute(IRuntimeStack stack, Object argsValues) {
        if (argsValues instanceof IFValueProviderArgs args) {
            // 执行if语句,if语句其实比较并不是很复杂,只需要判断条件,然后执行对应的分支即可,但需要按照声明顺序执行,且只能执行一个分支
            // 每一个分支都有两个参数,一个是条件,一个是执行体,参数由一个函数提供,该函数的返回值是一个布尔值,
            // 执行体也是一个函数,该函数可能可能无返回值
        }
        throw new IllegalArgumentException("argsValues must be a IFValueProviderArgs, but it is " + argsValues.getClass().getName());
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        return null;
    }
}
