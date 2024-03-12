package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Map或者说是Object类型的参数解析策略
 */
public class MapParameterParseStrategy extends AbstractParameterParseStrategy {

    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        FunctionCallInfo functionCallInfo = this.getFunctionCallInfo(parameter);
        Object preparseMap = Optional.ofNullable(functionCallInfo).map(fci -> fci.call(stack)).orElse(new HashMap<>());
        if (!(parameter instanceof Map<?, ?>)) {
            throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值不是一个Map");
        }
        if (parameter.isSkipChildFlag()) {
            if (((Map<String, Object>) preparseMap).isEmpty()) {
                if (parameter.isRequired()) {
                    throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值为空,但是该参数是必须的");
                }
                return preparseMap;
            }
            return preparseMap;
        }

        // 此时子参数无法获取parent变量
        Map<String, Object> parent = ((Map<String, Object>) preparseMap);
        Map<String, Object> presetVariables = new HashMap<>();
        presetVariables.put("$parent", preparseMap);
        parameter.getItems()
                .forEach(item -> {
                    // 创建子堆栈,解析子参数
                    IRuntimeStack childStack = stack.createChildStack("map[%s]".formatted(item.getName()), getFunctionCallInfo(item), presetVariables);
                    Object itemValue = this.delegateParse(item, childStack);
                    parent.put(item.getName(), itemValue);
                });
        if (parent.isEmpty()) {
            if (parameter.isRequired()) {
                throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值为空,但是该参数是必须的");
            }
            return null;
        }
        return parent;
    }

    @Override
    public String getSupportTypeName() {
        return Constants.Type.TYPE_NAME_MAP;
    }
}
