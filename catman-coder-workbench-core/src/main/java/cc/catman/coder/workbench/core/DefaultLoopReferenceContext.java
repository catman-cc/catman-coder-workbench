package cc.catman.coder.workbench.core;

import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class DefaultLoopReferenceContext implements ILoopReferenceContext {

    @Getter
    private final Map<String, TypeDefinition> typeDefinitions=new HashMap<>();
    @Getter
    private final Map<String, ValueProviderDefinition> valueProviderDefinitions=new HashMap<>();
    @Getter
    private final Map<String, Parameter> parameters=new HashMap<>();
    @Getter
    private final Map<String, FunctionInfo> functionInfos=new HashMap<>();

    public static DefaultLoopReferenceContext create(){
        return new DefaultLoopReferenceContext();
    }

    @Override
    public ILoopReferenceContext merge(ILoopReferenceContext context) {
        if (context!=null){
            this.typeDefinitions.putAll(context.getTypeDefinitions());
            this.valueProviderDefinitions.putAll(context.getValueProviderDefinitions());
            this.parameters.putAll(context.getParameters());
            this.functionInfos.putAll(context.getFunctionInfos());
        }
        return this;
    }
}
