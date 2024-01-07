package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.Data;

import java.util.Map;

@Data
public class Command {
    /**
     * 命令的名称
     */
    private String name;

    private String batchId;

    /**
     * 命令的直接参数
     */
    private Map<String,Object> variables;

    private ValueProviderDefinition valueProviderDefinition;
}
