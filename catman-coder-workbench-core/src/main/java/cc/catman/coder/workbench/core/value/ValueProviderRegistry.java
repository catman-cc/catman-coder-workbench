package cc.catman.coder.workbench.core.value;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ValueProviderRegistry {

    private Map<String,ValueProviderFactory> valueProviderFactories=new HashMap<>();

    public ValueProviderRegistry() {
    }

    public ValueProviderRegistry add(String kind, ValueProviderFactory factory){
        valueProviderFactories.put(kind,factory);
        return this;
    }

    public ValueProvider parse(ValueProviderDefinition valueProviderDefinition,ValueProviderContext context){
      return parse(valueProviderDefinition,context,new HashMap<>());
    }

    public ValueProvider parse(ValueProviderDefinition valueProviderDefinition,ValueProviderContext context,Map<String,Object> presetVariables){
        String kind = valueProviderDefinition.getKind();
        // 此时应该优先调用并处理前置处理器
        valueProviderDefinition.getPreValueProviders().forEach(pvp->{
            ValueProvider vp = parse(pvp, context,presetVariables);
            Object value = context.exec(vp,presetVariables);
            // 此时将变量放入上下文中
            context.addVariable(pvp.getName(),value);
        });

        // 在初始化当前值提供者之前,应该先初始化当前值提供者所依赖的值提供者
        // 依赖的值提供者应该在当前值提供者之前执行
        // 然后再创建当前值提供者
        ValueProviderFactory factory = valueProviderFactories.get(kind);
        Optional.ofNullable(factory).orElseThrow(()->new RuntimeException("未找到值提供者工厂:"+kind));
        return factory.createValueProvider(valueProviderDefinition,this,context.createChildContext(presetVariables));
    }
}
