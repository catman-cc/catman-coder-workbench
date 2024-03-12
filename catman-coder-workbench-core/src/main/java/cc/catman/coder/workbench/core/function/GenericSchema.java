package cc.catman.coder.workbench.core.function;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import lombok.Data;

@Data
public class GenericSchema {
    private String root;

    private String type;

    private ILoopReferenceContext context;

    public static GenericSchema of(FunctionCallInfo functionCallInfo){
        GenericSchema genericSchema=new GenericSchema();
        genericSchema.root=functionCallInfo.getId();
        genericSchema.context=functionCallInfo.getContext();
        genericSchema.type="function-call";
        return genericSchema;
    }
}
