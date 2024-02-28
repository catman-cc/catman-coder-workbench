package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.PropertyAccessor;
import org.springframework.core.convert.TypeDescriptor;

import java.util.*;

/**
 * 默认的参数解析处理器上下文
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class DefaultParameterParseHandlerContext implements IParameterParseHandlerContext {
    /**
     * 上下文中对应的值
     */
    @Builder.Default
    private Map<String,Object> variables=new HashMap<>();

    @Builder.Default
    private List<IParameterParseStrategy> parameterParseStrategies= new ArrayList<>();



    /**
     * 所属的值提取器上下文
     */
    private ValueProviderContext valueProviderContext;

    public Map<String,Object> buildVariables(){
        // 构建变量时,需要将父上下文中的变量也包含进来
        // 由于父上下文中的变量是不可变的,所以这里使用的是一个新的Map来存储变量
        Map<String,Object> variables=new HashMap<>(valueProviderContext.buildVariables());
        // 将当前上下文中的变量覆盖父上下文中的变量
        variables.putAll(this.variables);
        return variables;
    }

    @Override
    public IParameterParseHandlerContext registerParameterParseStrategy(IParameterParseStrategy strategy) {
        this.parameterParseStrategies.add(strategy);
        return this;
    }

    protected PropertyAccessor createPropertyAccessor(Object target){
        return this.valueProviderContext.createPropertyAccessor(target);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(Parameter parameter, TypeDescriptor typeDescriptor) {
        // 解析参数,本质上就是迭代参数结构,然后根据每一个参数对应的值提取器来提取值的过程
        Optional<IParameterParseStrategy> firstStrategy = this.parameterParseStrategies.stream().filter(strategy -> strategy.support(parameter)).findFirst();
        if (firstStrategy.isEmpty()){
            throw new RuntimeException("不支持的参数类型:"+parameter.getType().getType());
        }
        Object value = firstStrategy.get().parse(parameter, this);
        return (T) this.valueProviderContext.conversionService().convert(value,typeDescriptor);
    }

    // 很明显,在解析Parameter时,多了一层上下文,多了的这一层上下文,主要是为了解决array问题
    private Object getChildValue(Parameter elements, Map<String, Object> presetVariables) {
        ValueProviderDefinition itemPD = Optional.ofNullable(elements.getValue()).orElse(elements.getDefaultValue());
        ValueProvider childValueProvider = valueProviderContext.getValueProviderRegistry().parse(itemPD,valueProviderContext);
        // 执行值提取器,获取值
        ValueProviderContext childContext = valueProviderContext.createChildContext(childValueProvider, presetVariables);
        return childContext.exec(childValueProvider);
    }
}
