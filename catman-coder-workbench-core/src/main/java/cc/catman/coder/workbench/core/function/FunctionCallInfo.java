package cc.catman.coder.workbench.core.function;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.*;
import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FunctionCallInfo extends Base implements IFunctionCallInfo {

    private String id;

    private String name;

    @Builder.Default
    private  Map<String,String> argIds=new LinkedHashMap<>();

    private String resultId;

    private String resultName;

    private String functionId;

    private String config;

    List<IFunctionCallExceptionHandler> exceptionHandlers;

    @JsonIgnore
    @Builder.Default
    private transient ILoopReferenceContext context=ILoopReferenceContext.create();

    private List<Breakpoint> breakpoints;

    @JsonIgnore
    public void setArgs(Map<String,Parameter> args){
        this.argIds.clear();
        args.forEach((key, value) -> this.addArg(key, value));
    }
    @JsonIgnore
    public Map<String,Parameter> getArgs(){
       return this.argIds.entrySet()
               .stream()
               .collect(Collectors.toMap(Map.Entry::getKey,
                       (e)-> this.context.getParameter(e.getValue())
                               .orElseThrow(()->new RuntimeException("参数不存在,id:" + e.getValue()))));
    }


    public FunctionCallInfo addArg(String name,Parameter parameter){
        this.argIds.put(name,parameter.getId());
        this.context.add(parameter);
        return this;
    }

    public void setResult(Parameter result) {
        this.context.add(result);
        this.resultId=result.getId();
    }

    public Parameter getResult(){
        return this.context.getParameter(this.resultId).orElse(null);
    }

    @Override
    public String resultName() {
        return this.resultName;
    }


    @Override
    @JsonIgnore
    public FunctionInfo getFunctionInfo() {
        return this.context.getFunctionInfo(this.functionId).orElse(null);
    }

    /**
     * 在指定的运行时栈上调用函数
     * @param stack 运行时栈
     * @return 函数执行结果
     */
    @Override
    public Object call(IRuntimeStack stack) {
        IFunctionCallResultInfo callRes = stack.call(this);
        if (callRes.hasException()){
            stack.reportException(callRes.getException());
        }
        if (callRes.hasResult()){
            return callRes.getResult();
        }
        return null;
    }
}
