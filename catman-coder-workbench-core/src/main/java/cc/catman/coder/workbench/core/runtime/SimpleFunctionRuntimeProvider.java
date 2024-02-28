package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleFunctionRuntimeProvider implements IFunctionRuntimeProvider {
    private final IFunctionVariablesTable variablesTable;

    @Getter
    private final List<Breakpoint> breakpoints;
    private final IFunctionCallInfo functionInfo;

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
        this.breakpoints= new ArrayList<>();
    }

    public SimpleFunctionRuntimeProvider(IFunctionVariablesTable variablesTable, List<Breakpoint> breakpoints, IFunctionCallInfo functionInfo) {
        this.variablesTable = variablesTable;
        this.breakpoints = breakpoints;
        this.functionInfo = functionInfo;
    }

    @Override
    public IFunctionVariablesTable getVariablesTable() {
        return variablesTable;
    }

    @Override
    public IFunctionCallInfo getFunctionInfo() {
        return functionInfo;
    }

    @Override
    public Map<String, Object> getOutOfBandData() {
        return null;
    }
}
