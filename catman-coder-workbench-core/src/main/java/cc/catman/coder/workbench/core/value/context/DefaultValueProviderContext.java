package cc.catman.coder.workbench.core.value.context;

import cc.catman.coder.workbench.core.parameter.DefaultParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.*;
import lombok.*;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultValueProviderContext implements ValueProviderContext {

    private String id;
    private String batchId;
    private String name;
    @Builder.Default
    private Map<String,Object> variables=new HashMap<>();
    @Builder.Default
    private List<IParameterParseStrategy> parameterParseStrategies= new ArrayList<>();

    protected ValueProviderContext parentContext;

    private GenericConversionService genericConversionService;

    private ValueProviderExecutor valueProviderExecutor;

    private ValueProviderRegistry valueProviderRegistry;

    private IValueProviderContextManager contextManager;



    public Map<String,Object> buildVariables(){
        // 构建上下文时,所进行的操作是依次获取父上下文中的所有变量,之后再由当前上下文中的变量覆盖父上下文中的变量
        // 由于父上下文中的变量是不可变的,所以这里使用的是一个新的Map来存储变量
        Map<String,Object> variables=getParentContext()
                .map(ValueProviderContext::buildVariables)
                .orElseGet(HashMap::new);
        // 将当前上下文中的变量覆盖父上下文中的变量
        variables.putAll(this.variables);
        return variables;
    }

    @Override
    public Map<String, Object> variables() {
        return variables;
    }

    @Override
    public Optional<ValueProviderContext> getParentContext() {
        return Optional.ofNullable(parentContext);
    }

    @Override
    public ValueProviderRegistry getValueProviderRegistry() {
        return this.valueProviderRegistry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object value, TypeDescriptor type) {
        return (T) this.conversionService().convert(value,type);
    }

    @Override
    public ExpressionParser createExpressionParser(String language) {
        return new SpelExpressionParser();
    }

    @Override
    public EvaluationContext createEvaluationContext(Map<String, Object> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext(variables);
        context.addPropertyAccessor(new MapAccessor());
        return context;
    }

    @Override
    public GenericConversionService conversionService() {
        return this.genericConversionService;
    }

    @Override
    public Map<String, Object> getVariables() {
        return this.variables;
    }

    @Override
    public Object getVariable(String name) {
        Expression expression = this.createExpressionParser("spel").parseExpression(name);
        return expression.getValue(this.createEvaluationContext(this.buildVariables()));
//        return this.createPropertyAccessor(this.buildVariables()).getPropertyValue(name);
    }

    @Override
    public void addVariable(String name, Object value) {
        this.variables.put(name,value);
    }

    @Override
    public <T> T parse(Parameter parameter, TypeDescriptor descriptor) {
        IParameterParseHandlerContext parameterParseHandlerContext = this.createParameterParseHandlerContext();
        return parameterParseHandlerContext.parse(parameter,descriptor);
    }

    @Override
    public ValueProvider parse(ValueProviderDefinition valueProviderDefinition, ValueProviderContext context, Map<String, Object> presetVariables) {
        return this.getValueProviderRegistry().parse(valueProviderDefinition,context,presetVariables);
    }

    @Override
    public ValueProviderContext createChildContext(ValueProvider valueProvider, Map<String, Object> variables) {
        String namePrefix=Optional.ofNullable(valueProvider).map(ValueProvider::getName).orElseGet(()-> this.getName().substring(0,12) + "-child");
        return DefaultValueProviderContext.builder()
                .id(namePrefix+UUID.randomUUID().toString().substring(0,6))
                .name(namePrefix+"--"+this.id)
                .batchId(this.batchId)
                .parentContext(this)
                .variables(new HashMap<>(variables))
                .valueProviderRegistry(this.valueProviderRegistry)
                .valueProviderExecutor(this.valueProviderExecutor)
                .genericConversionService(this.genericConversionService)
                .contextManager(this.contextManager)
                .parameterParseStrategies(this.parameterParseStrategies)
                .build()
                .register();
    }

    @Override
    public IParameterParseHandlerContext createParameterParseHandlerContext() {

        return DefaultParameterParseHandlerContext.builder().valueProviderContext(this)
                .parameterParseStrategies(parameterParseStrategies)
                .build();
    }

    @Override
    public ValueProviderExecutor getValueProviderExecutor() {
        return this.valueProviderExecutor;
    }
}
