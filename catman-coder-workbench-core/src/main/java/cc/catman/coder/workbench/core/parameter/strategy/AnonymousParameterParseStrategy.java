package cc.catman.coder.workbench.core.parameter.strategy;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;

import java.util.Map;
import java.util.Optional;

/**
 * 匿名类型参数解析策略
 * 该策略和Map类型的参数解析策略基本一致,但是匿名类型参数,本身不支持取值器,因此在解析参数时,会忽略取值器
 * <p>
 * 同时匿名对象本身不持有上下文,因此在解析子参数时,会直接讲子参数的值传递给父级上下文变量
 */
public class AnonymousParameterParseStrategy implements IParameterParseStrategy {
    @Override
    public boolean support(Parameter parameter) {
        return parameter.getType().getType().isAnonymous() || checkTypeName(parameter);
    }

    protected boolean checkTypeName(Parameter parameter) {
        DefaultType type = parameter.getType().getType();
        return Constants.Type.TYPE_NAME_ANONYMOUS.equals(type.getTypeName());
    }

    @Override
    public Object parse(Parameter parameter, IParameterParseHandlerContext parameterParseHandlerContext) {
        // 判断是否有子参数,如果没有子参数,那么就直接返回null
        if (parameter.getItems().isEmpty()) {
            return null;
        }
        ValueProviderContext valueProviderContext = parameterParseHandlerContext.getValueProviderContext().getParentContext()
                .orElseThrow(() -> new RuntimeException("匿名类型的参数必须有父级上下文"));
        Map<String, Object> parent = valueProviderContext.getVariables();
        // 如果有子参数,那么就需要使用子参数提取器来构建map
        parameter.getItems()
                .forEach(item -> {
                    ValueProviderDefinition itemValueProviderDefinition = Optional.ofNullable(item.getValue()).orElse(item.getDefaultValue());
                    Optional.ofNullable(itemValueProviderDefinition).ifPresent(ivpd -> {
                        ValueProvider childValueProvider = valueProviderContext.getValueProviderRegistry().parse(ivpd, valueProviderContext);
                        ValueProviderContext childContext = valueProviderContext.createChildContext(childValueProvider);
                        Object itemValue = childContext.exec(ivpd);
                        valueProviderContext.addVariable(item.getName(), itemValue);
                        parent.put(item.getName(), itemValue);
                    });
                });
        return parent;
    }
}
