package cc.catman.coder.workbench.core.runtime.debug;

import lombok.Data;

/**
 * 断点信息
 */
@Data
public class BreakpointInformation {
    /**
     * 断点名称
     */
    private String name;

    /**
     * 断点描述信息
     */
   private String describe;
}
