package cc.catman.coder.workbench.core.parameter;

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
     * 参数定义
     */
    private Map<String,Parameter> items;

    private Map<String, TypeDefinition> typeDefinitions;
}
