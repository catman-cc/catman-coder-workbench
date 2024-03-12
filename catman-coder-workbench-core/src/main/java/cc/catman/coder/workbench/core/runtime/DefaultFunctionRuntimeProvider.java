package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import java.util.List;

public class DefaultFunctionRuntimeProvider extends AbstractFunctionRuntimeProvider {

    public static DefaultFunctionRuntimeProvider create(IFunctionCallInfo functionInfo) {
        return new DefaultFunctionRuntimeProvider(new DefaultFunctionVariablesTable(), functionInfo);
    }
    public static DefaultFunctionRuntimeProvider create(IFunctionCallInfo functionInfo, IFunctionVariablesTable variablesTable) {
        return new DefaultFunctionRuntimeProvider(variablesTable, functionInfo);
    }

    public static DefaultFunctionRuntimeProvider create(IFunctionCallInfo functionInfo, List<Breakpoint> breakpoints) {
        return new DefaultFunctionRuntimeProvider(new DefaultFunctionVariablesTable(), breakpoints, functionInfo);
    }

    public static DefaultFunctionRuntimeProvider create(IFunctionCallInfo functionInfo, IFunctionVariablesTable variablesTable, List<Breakpoint> breakpoints) {
        return new DefaultFunctionRuntimeProvider(variablesTable, breakpoints, functionInfo);
    }

    public DefaultFunctionRuntimeProvider(IFunctionVariablesTable variablesTable, IFunctionCallInfo functionInfo) {
        this.variablesTable = variablesTable;
        this.functionInfo = functionInfo;
    }

    public DefaultFunctionRuntimeProvider(IFunctionVariablesTable variablesTable, List<Breakpoint> breakpoints, IFunctionCallInfo functionInfo) {
        this.variablesTable = variablesTable;
        this.breakpoints = breakpoints;
        this.functionInfo = functionInfo;
    }
}
