package cc.catman.coder.workbench.core.runtime.debug;

import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;
import lombok.Data;

/**
 * 断点,代码执行到此处时,
 */
@Data
public class Breakpoint {

    private String id;

    /**
     * 断点对应的函数id
     */
    private String functionId;

    /**
     * 断点对应的函数调用id
     */
    private String functionCallId;

    /**
     * 断点对应的行号,此处的行号和代码行号不一样,这里的行号其实是Function提供的断点index
     */
    private int lineNumber;

    private  boolean isEnable;

    /**
     * 断点对应的触发器
     */
    private IFunctionRuntimeProvider assertProvider;

    /**
     * 对应的断点信息
     */
    private BreakpointInformation information;

}
