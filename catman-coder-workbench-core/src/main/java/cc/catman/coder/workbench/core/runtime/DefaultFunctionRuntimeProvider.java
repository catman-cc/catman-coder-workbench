package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import java.util.List;

public class SimpleFunctionRuntimeProvider extends AbstractFunctionRuntimeProvider {

    public static SimpleFunctionRuntimeProvider create(IFunctionCallInfo functionInfo) {
        return new SimpleFunctionRuntimeProvider(new DefaultFunctionVariablesTable(), functionInfo);
    }
    public static SimpleFunctionRuntimeProvider create( IFunctionCallInfo functionInfo,IFunctionVariablesTable variablesTable) {
        return new SimpleFunctionRuntimeProvider(variablesTable, functionInfo);
    }

    public static SimpleFunctionRuntimeProvider create(IFunctionCallInfo functionInfo, List<Breakpoint> breakpoints) {
        return new SimpleFunctionRuntimeProvider(new DefaultFunctionVariablesTable(), breakpoints, functionInfo);
    }

    public static SimpleFunctionRuntimeProvider create(IFunctionCallInfo functionInfo, IFunctionVariablesTable variablesTable, List<Breakpoint> breakpoints) {
        return new SimpleFunctionRuntimeProvider(variablesTable, breakpoints, functionInfo);
    }

    public SimpleFunctionRuntimeProvider(IFunctionVariablesTable variablesTable, IFunctionCallInfo functionInfo) {
        this.variablesTable = variablesTable;
        this.functionInfo = functionInfo;
    }

    public SimpleFunctionRuntimeProvider(IFunctionVariablesTable variablesTable, List<Breakpoint> breakpoints, IFunctionCallInfo functionInfo) {
        this.variablesTable = variablesTable;
        this.breakpoints = breakpoints;
        this.functionInfo = functionInfo;
    }
}
