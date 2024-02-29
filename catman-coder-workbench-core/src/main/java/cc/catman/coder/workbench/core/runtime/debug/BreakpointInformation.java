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

    /**
     * 真实断点对应的函数id
     */
   private String matchFunctionId;

   /**
    * 真实断点对应的函数行号
    */
   private String matchFunctionLine;
}
