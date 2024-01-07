package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import org.springframework.core.convert.TypeDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 值提供者执行器
 */
public interface ValueProviderExecutor {

    Object exec(ValueProvider valueProvider);

    List<IParameterParseStrategy> getParameterParseStrategies();

    default <T> T exec(ValueProvider valueProvider,Class<T> resultType){
        return exec(valueProvider,null,null,resultType);
    }

    default <T> T exec(ValueProvider valueProvider,TypeDescriptor resultType){
        return exec(valueProvider,null,null,resultType);
    }

    default <T> T exec(ValueProvider valueProvider,Map<String, Object> presetVariables){
        return (T) exec(valueProvider,null,presetVariables, Object.class);
    }
    default <T> T exec(ValueProvider valueProvider,Map<String, Object> presetVariables,Class<T> resultType){
        return exec(valueProvider,null,presetVariables,resultType);
    }

    default <T> T exec(ValueProvider valueProvider,Map<String, Object> presetVariables,TypeDescriptor resultType){
        return exec(valueProvider,null,presetVariables,resultType);
    }


    default Object exec(ValueProvider valueProvider,ValueProviderContext parentContext){
        return exec(valueProvider,parentContext,null);
    }

    default Object exec(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables){
        return exec(valueProvider,parentContext,presetVariables,Object.class);
    }

    <T> T exec(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables,Class<T> resultType);

    <T> T exec(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables, TypeDescriptor resultType);

    default Object exec(ValueProviderDefinition valueProviderDefinition,Map<String,Object> variables){
        return exec(valueProviderDefinition,variables,Object.class);
    }

    <T> T exec(ValueProviderDefinition valueProviderDefinition,Map<String,Object> variables,Class<T> resultType);

    <T> T exec(ValueProviderDefinition valueProviderDefinition,Map<String,Object> variables,TypeDescriptor resultType);

    default Object exec(ValueProviderDefinition valueProviderDefinition){
        return exec(valueProviderDefinition,new HashMap<>());
    }

    default <T> T exec(ValueProviderDefinition valueProviderDefinition,Class<T> resultType){
        return exec(valueProviderDefinition,new HashMap<>(),resultType);
    }

    default <T> T exec(ValueProviderDefinition valueProviderDefinition,TypeDescriptor resultType){
        return exec(valueProviderDefinition,new HashMap<>(),resultType);
    }
      ValueProviderContext createValueProviderContext(ValueProvider valueProvider, Map<String,Object> presetVariables);
      ValueProviderContext createValueProviderContext(ValueProviderDefinition valueProviderDefinition, Map<String,Object> presetVariables);
}
