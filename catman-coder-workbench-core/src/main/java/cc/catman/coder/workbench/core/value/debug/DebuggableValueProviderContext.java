package cc.catman.coder.workbench.core.value.debug;

import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.*;
import lombok.*;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 调试用的值提供者上下文,该上下文接受外部控制,用于调试
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebuggableValueProviderContext implements ValueProviderContext {

    private String id;
    private String batchId;
    private String name;
    private Optional<ValueProviderContext> parentContext;
    private ValueProviderContext proxy;


    @Override
    public ValueProviderContext createChildContext(ValueProvider valueProvider, Map<String, Object> variables) {
        ValueProviderContext pvpc = proxy.createChildContext(valueProvider, variables);
        return DebuggableValueProviderContext.builder()
                .id(pvpc.getId())
                .batchId(pvpc.getBatchId())
                .name(pvpc.getName())
                .proxy(pvpc)
                .parentContext(Optional.of(this))
                .build();
    }


    @Override
    public IParameterParseHandlerContext createParameterParseHandlerContext() {
        return null;
    }

    @Override
    public ValueProviderExecutor getValueProviderExecutor() {
        return null;
    }

    @Override
    public EvaluationContext createEvaluationContext(Map<String, Object> variables) {
        return null;
    }

    @Override
    public Object getVariable(String name) {
        return proxy.getVariable(name);
    }
    @Override
    public Map<String, Object> variables() {
        return proxy.variables();
    }
    @Override
    public Map<String, Object> buildVariables() {
        return proxy.buildVariables();
    }

    @Override
    public Map<String, Object> getVariables() {
        return proxy.getVariables();
    }
    @Override
    public void addVariable(String name, Object value) {
        proxy.addVariable(name,value);
    }
    @Override
    public Optional<ValueProviderContext> getParentContext() {
        return this.parentContext;
    }

    @Override
    public IValueProviderContextManager getContextManager() {
        return null;
    }

    @Override
    public ValueProviderRegistry getValueProviderRegistry() {
        return proxy.getValueProviderRegistry();
    }

    @Override
    public List<IParameterParseStrategy> getParameterParseStrategies() {
        return proxy.getParameterParseStrategies();
    }

    @Override
    public <T> T convert(Object value, TypeDescriptor type) {
        return proxy.convert(value, type);
    }

    @Override
    public ExpressionParser createExpressionParser(String language) {
        return proxy.createExpressionParser(language);
    }

    @Override
    public GenericConversionService conversionService() {
        return proxy.conversionService();
    }


    @Override
    public <T> T parse(Parameter parameter, TypeDescriptor descriptor) {
        return proxy.parse(parameter, descriptor);
    }

    @Override
    public ValueProvider parse(ValueProviderDefinition valueProviderDefinition, ValueProviderContext context, Map<String, Object> presetVariables) {
        return proxy.parse(valueProviderDefinition, context, presetVariables);
    }

}
