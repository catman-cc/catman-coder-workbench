package cc.catman.workbench.api.server.websocket.run.value.breakpoint;

import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.Data;

@Data
public class BreakPoint {
    /**
     * 断点路径,用于标识断点所在的位置
     */
    private String[] path;

    /**
     * 断点类型
     */
    private EBreakPointType type;

    /**
     * 断点条件
     */
    private ValueProviderDefinition condition;

    /**
     * 任务执行前触发断点
     */
    private boolean before;

    /**
     * 任务执行后触发断点
     */
    private boolean after;
}
