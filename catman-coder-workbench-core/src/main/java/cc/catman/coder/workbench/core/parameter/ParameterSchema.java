package cc.catman.coder.workbench.core.parameter;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import lombok.Data;

import java.util.Map;

/**
 * 通过对参数定义进行一层封装,实现可循环引用的参数定义
 */
@Data
public class ParameterSchema {
    /*
     * 根节点的id
     */
    private String root;

    /**
     * 循环引用上下文
     */
    private ILoopReferenceContext context;

    public static ParameterSchema of(Parameter parameter){
        ParameterSchema schema=new ParameterSchema();
        schema.setRoot(parameter.getId());
        schema.setContext(parameter.getContext());
        return schema;
    }
}
