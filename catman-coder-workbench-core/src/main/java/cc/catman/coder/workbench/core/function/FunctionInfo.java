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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Getter
    @Setter
    private Map<String,TypeDefinition> args;

    /**
     * 函数的返回值
     */
    @Getter
    @Setter
    private TypeDefinition result;
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
