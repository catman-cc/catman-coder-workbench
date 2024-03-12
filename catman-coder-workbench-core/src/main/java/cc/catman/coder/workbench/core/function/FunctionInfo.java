package cc.catman.coder.workbench.core.function;

import cc.catman.coder.workbench.core.Base;
import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.coder.workbench.core.runtime.debug.BreakpointInformation;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.checkerframework.checker.units.qual.A;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 函数定义
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionInfo extends Base implements IFunctionInfo {
    /**
     * 唯一标志
     */
    @Getter
    @Setter
    private String  id;

    /**
     * 函数名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 函数类型
     */
    @Getter
    @Setter
    private String kind;

    /**
     * 函数的简短描述
     */
    @Getter
    @Setter
    private String describe;

    /**
     * 函数的参数
     */
    @Builder.Default
    private Map<String,String> argIds=new LinkedHashMap<>();

    private String resultId;


    public Map<String,TypeDefinition> getArgs(){
        return this.argIds.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        (e)-> this.context.getTypeDefinition(e.getValue())
                                .orElseThrow(()->new RuntimeException("类型定义不存在,id:" + e.getValue()))));
    }

    public void setArgs(Map<String,TypeDefinition> args){
        this.argIds.clear();
        args.forEach(this::addArg);
    }

    public FunctionInfo addArg(String name,TypeDefinition typeDefinition){
        this.argIds.put(name,typeDefinition.getId());
        this.context.add(typeDefinition);
        return this;
    }

    public FunctionInfo addArgs(Map<String,TypeDefinition> args){
        args.forEach(this::addArg);
        return this;
    }

    public FunctionInfo addArgs(List<TypeDefinition> args){
        args.forEach(arg->{
            this.addArg(arg.getName(),arg);
        });
        return this;
    }

    public void setResult(TypeDefinition result) {
        this.context.add(result);
        this.resultId=result.getId();
    }

    public TypeDefinition getResult(){
        return this.context.getTypeDefinition(this.resultId).orElse(null);
    }

    @Setter
    @Builder.Default
    List<FunctionCallInfo> callQueue=new ArrayList<>();

    public List<FunctionCallInfo> getCallQueue() {
        return callQueue;
    }

    @Getter
    @Setter
    @Builder.Default
    List<FunctionCallInfo> finallyCalls=new ArrayList<>();
    @Getter
    @Setter
    @Builder.Default
    List<Object> exceptionHandlers=new ArrayList<>();
    @Getter
    @Setter
    @JsonIgnore
    @Builder.Default
    private transient ILoopReferenceContext context=ILoopReferenceContext.create();

    @Override
    public List<BreakpointInformation> getBreakpointInformation() {
        return null;
    }
}
