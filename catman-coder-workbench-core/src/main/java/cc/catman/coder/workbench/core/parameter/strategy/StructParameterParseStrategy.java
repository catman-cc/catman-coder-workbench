package cc.catman.coder.workbench.core.parameter.strategy;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import org.springframework.expression.ExpressionParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StructParameterParseStrategy implements IParameterParseStrategy {
    @Override
    public boolean support(Parameter parameter) {
        return parameter.getType().getType().isStruct() || checkTypeName(parameter);
    }
    protected boolean checkTypeName(Parameter parameter){
        DefaultType type = parameter.getType().getType();
        return Constants.Type.TYPE_NAME_STRUCT.equals(type.getTypeName());
    }
    @Override
    public Object parse(Parameter parameter, IParameterParseHandlerContext parameterParseHandlerContext) {
        ValueProviderDefinition vpd = Optional.ofNullable(parameter.getValue()).orElse(parameter.getDefaultValue());
        if (Optional.ofNullable(vpd).isEmpty()) {
            if (parameter.isSkipChildFlag()) {
                if (parameter.isRequired()) {
                    throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值为空,但是该参数是必须的");
                }
                return null;
            }
            // 判断是否有子参数,如果没有子参数,那么就直接返回null
            if (parameter.getItems().isEmpty()) {
                return null;
            }

            ValueProviderContext valueProviderContext = parameterParseHandlerContext.getValueProviderContext();

            // 如果有子参数,那么就需要使用子参数提取器来构建map
            // 此时子参数无法获取parent变量
            Map<String, Object> parent = new HashMap<>();
            parameter.getItems()
                    .forEach(item -> {
                        ValueProviderDefinition itemValueProviderDefinition = Optional.ofNullable(item.getValue()).orElse(item.getDefaultValue());
                        Optional.ofNullable(itemValueProviderDefinition).ifPresent(ivpd -> {
                            ValueProvider childValueProvider = valueProviderContext.getValueProviderRegistry().parse(ivpd, valueProviderContext);
                            ValueProviderContext childContext = valueProviderContext.createChildContext(childValueProvider);
                            Object itemValue = childContext.exec(ivpd);
                            // 如果子参数有值,那就替换上述集合中的值
                            parent.put(item.getName(), itemValue);
                        });
                    });
            if (parent.isEmpty()) {
                if (parameter.isRequired()){
                    throw new RuntimeException("参数" + parameter.getName() + "的值提取器返回的值为空,但是该参数是必须的");
                }
                return null;
            }
            return parent;
        }

        // 如果有值提取器,那么就使用值提取器来预构建map
        ValueProviderContext valueProviderContext = parameterParseHandlerContext.getValueProviderContext();
        Map<String, Object> preset = new HashMap<>();
        preset.put("__parameter__name__", parameter.getName());
        ValueProvider paramValueProvider = valueProviderContext.parse(vpd, preset);
        Object preparseMap = valueProviderContext.exec(paramValueProvider, preset);
        if (parameter.isSkipChildFlag()) {
            // 如果跳过子参数,那么就直接返回预解析集合
            return preparseMap;
        }
        // 如果不跳过子参数,那么就需要使用子参数提取器来构建map
        // 此时子参数可以获取parent变量
        Map<String, Object> presetVariables = new HashMap<>();
        presetVariables.put("parent",preparseMap);
        ExpressionParser parser = valueProviderContext.createExpressionParser("SpEL");
        // 考虑此处,如何递归解析子参数
        parameter.getItems()
                .forEach(item -> {
                    ValueProviderDefinition itemValueProviderDefinition = Optional.ofNullable(item.getValue()).orElse(item.getDefaultValue());
                    Optional.ofNullable(itemValueProviderDefinition).ifPresent(ivpd -> {
                        ValueProvider childValueProvider = valueProviderContext.getValueProviderRegistry().parse(ivpd, valueProviderContext);
                        ValueProviderContext childContext = valueProviderContext.createChildContext(childValueProvider, presetVariables);
                        Object itemValue = childContext.exec(ivpd);
                        // 如果子参数有值,那就替换上述集合中的值
                        parser.parseExpression(item.getName()).setValue(preparseMap, itemValue);
                    });
                });
        return preparseMap;
    }
}
