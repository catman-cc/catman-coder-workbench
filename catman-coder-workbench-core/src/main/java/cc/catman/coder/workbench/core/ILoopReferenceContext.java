package cc.catman.coder.workbench.core;

import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;
import java.util.function.Function;

/**
 * 循环引用上下文,用于解决循环引用问题
 */
@JsonDeserialize(as=DefaultLoopReferenceContext.class)
public interface ILoopReferenceContext {

    static ILoopReferenceContext create(){
        return DefaultLoopReferenceContext.create();
    }

    default boolean filter(Base base){
//       return Optional.ofNullable(base).map(b->b.getScope().isPublic()).orElse(false);
        // 2024年01月27日 尝试将所有对象都加入到上下文中
        return  true;
    }

    Map<String, TypeDefinition> getTypeDefinitions();

    Map<String, ValueProviderDefinition> getValueProviderDefinitions();

    Map<String, Parameter> getParameters();

    Map<String, FunctionInfo> getFunctionInfos();

    Map<String, FunctionCallInfo> getFunctionCallInfos();

    ILoopReferenceContext merge(ILoopReferenceContext context);


    default boolean includeTypeDefinition(String id) {
        return getTypeDefinitions().containsKey(id);
    }

    default boolean includeValueProviderDefinition(String id) {
        return getValueProviderDefinitions().containsKey(id);
    }

    default boolean includeParameter(String id) {
        return getParameters().containsKey(id);
    }

    default boolean includeFunctionInfo(String id) {
        return getFunctionInfos().containsKey(id);
    }

    default Optional<Parameter> getParameter(String id) {
        if (getParameters().containsKey(id)) {
            return Optional.ofNullable(getParameters().get(id));
        }
        return Optional.empty();
    }

    /**
     * TODO: 获取参数对象,现在要求creator方法尽可能早的将对象加入到上下文中,避免内部再次引用自身导致堆栈溢出.
     * 目前这种不是一个好的实现方案,理论上通过在获取时应该通过切面拦截创建一个proxy对象,自动加入到上下文中.
     * 但目前没有时间解决这个问题,优先级较低.
     * @param id 参数id
     * @param creator 创建参数对象的方法,如果参数对象已经存在,则不会调用此方法
     * @return 参数对象
     */
    default Optional<Parameter> getParameter(String id, Function<ILoopReferenceContext, Optional<Parameter>> creator) {
        if (getParameters().containsKey(id)) {
            return Optional.ofNullable(getParameters().get(id));
        }
        return creator.apply(this).map(parameter -> {
            if (this.filter(parameter)){
                if (!getParameters().containsKey(id)) {
                    getParameters().put(id, parameter);
                }
            }
            return parameter;
        });
    }

    default Optional<TypeDefinition> getTypeDefinition(String id) {
        if (getTypeDefinitions().containsKey(id)) {
            return Optional.ofNullable(getTypeDefinitions().get(id));
        }
        return Optional.empty();
    }
    default Optional<TypeDefinition> getTypeDefinition(String id, Function<ILoopReferenceContext, Optional<TypeDefinition>> creator) {
        if (getTypeDefinitions().containsKey(id)) {
            return Optional.ofNullable(getTypeDefinitions().get(id));
        }
        return creator.apply(this).map(typeDefinition -> {
            if(this.filter(typeDefinition)){
                if (!getTypeDefinitions().containsKey(id)) {
                    getTypeDefinitions().put(id, typeDefinition);
                }
            }
            return typeDefinition;
        });
    }

    default Optional<ValueProviderDefinition> getValueProviderDefinition(String id) {
        if (getValueProviderDefinitions().containsKey(id)) {
            return Optional.ofNullable(getValueProviderDefinitions().get(id));
        }
        return Optional.empty();
    }
    default Optional<ValueProviderDefinition> getValueProviderDefinition(String id, Function<ILoopReferenceContext, Optional<ValueProviderDefinition>> creator) {
        if (getValueProviderDefinitions().containsKey(id)) {
            return Optional.ofNullable(getValueProviderDefinitions().get(id));
        }
        return creator.apply(this).map(valueProviderDefinition -> {
            if (this.filter(valueProviderDefinition)){
                if (!getValueProviderDefinitions().containsKey(id)) {
                    getValueProviderDefinitions().put(id, valueProviderDefinition);
                }
            }
            return valueProviderDefinition;
        });
    }

    default Optional<FunctionInfo> getFunctionInfo(String id) {
        if (getFunctionInfos().containsKey(id)) {
            return Optional.ofNullable(getFunctionInfos().get(id));
        }
        return Optional.empty();
    }

    default Optional<FunctionInfo> getFunctionInfo(String id, Function<ILoopReferenceContext, Optional<FunctionInfo>> creator) {
        if (getFunctionInfos().containsKey(id)) {
            return Optional.ofNullable(getFunctionInfos().get(id));
        }
        return creator.apply(this).map(functionInfo -> {
            if (this.filter(functionInfo)){
                if (!getFunctionInfos().containsKey(id)) {
                    getFunctionInfos().put(id, functionInfo);
                }
            }
            return functionInfo;
        });
    }

    default boolean add(Parameter parameter) {
        if (parameter == null) {
            return false;
        }
        if (this.filter(parameter)) {
            if (!this.equals(parameter.getContext())){
                parameter.setContext(this.merge(parameter.getContext()));
            }
            getParameters().put(parameter.getId(), parameter);
            return  true;
        }
        return false;
    }

    default boolean add(TypeDefinition typeDefinition) {
        if (typeDefinition == null) {
            return false;
        }
        if (this.filter(typeDefinition)) {
            if (!this.equals(typeDefinition.getContext())){
                typeDefinition.setContext(this.merge(typeDefinition.getContext()));
            }
            getTypeDefinitions().put(typeDefinition.getId(), typeDefinition);
            return  true;
        }
        return false;
    }

    default boolean add(ValueProviderDefinition valueProviderDefinition) {
        if (valueProviderDefinition == null) {
            return false;
        }
        if (this.filter(valueProviderDefinition)) {
            if (!this.equals(valueProviderDefinition.getContext())){
                valueProviderDefinition.setContext(this.merge(valueProviderDefinition.getContext()));
            }
            getValueProviderDefinitions().put(valueProviderDefinition.getId(), valueProviderDefinition);
            return  true;
        }
        return false;
    }

    default boolean add(FunctionInfo functionInfo) {
        if (functionInfo == null) {
            return false;
        }
        if (this.filter(functionInfo)) {
            if (!this.equals(functionInfo.getContext())){
                functionInfo.setContext(this.merge(functionInfo.getContext()));
            }
            getFunctionInfos().put(functionInfo.getId(), functionInfo);
            return  true;
        }
        return false;
    }

    default boolean add(FunctionCallInfo functionCallInfo) {
        if (functionCallInfo == null) {
            return false;
        }
        if (this.filter(functionCallInfo)) {
            if (!this.equals(functionCallInfo.getContext())){
                functionCallInfo.setContext(this.merge(functionCallInfo.getContext()));
            }
            getFunctionCallInfos().put(functionCallInfo.getId(), functionCallInfo);
            return  true;
        }
        return false;
    }

    default void addParameters(Collection<Parameter> parameters) {
        if (parameters == null) {
           return;
        }
        parameters.forEach(this::add);
    }

    default void addTypeDefinitions(Collection<TypeDefinition> typeDefinitions) {
        if (typeDefinitions == null) {
           return;
        }
        typeDefinitions.forEach(this::add);
    }

    default void addValueProviderDefinitions(Collection<ValueProviderDefinition> valueProviderDefinitions) {
        if (valueProviderDefinitions == null) {
           return;
        }
        valueProviderDefinitions.forEach(this::add);
    }

    default void addFunctionInfos(Collection<FunctionInfo> functionInfos) {
        if (functionInfos == null) {
           return;
        }
        functionInfos.forEach(this::add);
    }

}
