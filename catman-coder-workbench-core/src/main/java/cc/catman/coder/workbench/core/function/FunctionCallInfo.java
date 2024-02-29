package cc.catman.coder.workbench.core.function;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IFunctionCallExceptionHandler;
import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;
import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FunctionCallInfo extends Base implements IFunctionCallInfo {

    private String id;

    private String name;

    private Map<String,Parameter> args;

    private Parameter result;

    private String resultName;

    private String functionId;

    List<IFunctionCallExceptionHandler> exceptionHandlers;

    @JsonIgnore
    @Builder.Default
    private transient ILoopReferenceContext context=ILoopReferenceContext.create();

    private List<Breakpoint> breakpoints;

    @Override
    public String resultName() {
        return this.resultName;
    }


    @Override
    @JsonIgnore
    public FunctionInfo getFunctionInfo() {
        return this.context.getFunctionInfo(this.functionId).orElse(null);
    }

    @Override
    public Object call(IRuntimeStack stack) {
        return null;
    }
}
