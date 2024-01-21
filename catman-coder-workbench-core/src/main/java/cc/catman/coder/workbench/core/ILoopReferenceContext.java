package cc.catman.coder.workbench.core;

import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 循环引用上下文,用于解决循环引用问题
 */
public interface ILoopReferenceContext {

    default boolean filter(Base base){
       return Optional.ofNullable(base).map(b->b.getScope().isPublic()).orElse(false);
    }

    Map<String, TypeDefinition> getTypeDefinitions();

    Map<String, ValueProviderDefinition> getValueProviderDefinitions();

    Map<String, Parameter> getParameters();

    Map<String, FunctionInfo> getFunctionInfos();

    ILoopReferenceContext merge(ILoopReferenceContext context);

    default Optional<Parameter> getParameter(String id) {
        if (getParameters().containsKey(id)) {
            return Optional.ofNullable(getParameters().get(id));
        }
        return Optional.empty();
    }
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
            getFunctionInfos().put(functionInfo.getId(), functionInfo);
            return  true;
        }
        return false;
    }

}
