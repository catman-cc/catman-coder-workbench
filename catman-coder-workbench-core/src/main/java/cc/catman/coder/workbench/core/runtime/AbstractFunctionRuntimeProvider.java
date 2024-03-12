package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFunctionRuntimeProvider implements IFunctionRuntimeProvider{
    protected   IFunctionVariablesTable variablesTable;

    protected  List<Breakpoint> breakpoints=new ArrayList<>();
    protected  IFunctionCallInfo functionInfo;

    protected Map<String,Object> outOfBandData=new HashMap<>();

    @Override
    public IFunctionVariablesTable getVariablesTable() {
        return this.variablesTable;
    }

    @Override
    public IFunctionCallInfo getFunctionInfo() {
        return this.functionInfo;
    }

    @Override
    public List<Breakpoint> getBreakpoints() {
        return this.breakpoints;
    }

    @Override
    public Map<String, Object> getOutOfBandData() {
        return this.outOfBandData;
    }


    @Override
    public IFunctionRuntimeProvider wrapper(IFunctionCallInfo functionInfo){
        DefaultFunctionRuntimeProvider p = new DefaultFunctionRuntimeProvider(variablesTable, breakpoints, functionInfo);
        p.outOfBandData=this.outOfBandData;
        return p;
    }

    public IFunctionRuntimeProvider copyTo(IFunctionCallInfo functionInfo) {
        return new DefaultFunctionRuntimeProvider(variablesTable, breakpoints, functionInfo);
    }

    /**
     * 将当前的运行时上下文复制到另一个运行时上下文中
     * 要求provider必须是AbstractFunctionRuntimeProvider的实例
     * @param provider 目标运行时上下文
     * @return 目标运行时上下文
     */
    public IFunctionRuntimeProvider copyTo(IFunctionRuntimeProvider provider){
        if (!(provider instanceof AbstractFunctionRuntimeProvider)){
            throw new IllegalArgumentException("provider must be instance of AbstractFunctionRuntimeProvider, but it is "+provider.getClass().getName());
        }
        AbstractFunctionRuntimeProvider p=(AbstractFunctionRuntimeProvider)provider;
        p.variablesTable=this.variablesTable;
        p.breakpoints=this.breakpoints;
        p.functionInfo=this.functionInfo;
        p.outOfBandData=this.outOfBandData;
        return p;
    }

    /**
     * 将当前的运行时上下文合并到另一个运行时上下文中.
     * 1. 不会将当前的functionInfo合并到目标运行时上下文中
     * 2. 按照变量级别,将当前的变量表合并到目标运行时上下文中,如果同级别出现同名变量,则优先使用已存在的变量
     * 要求provider必须是AbstractFunctionRuntimeProvider的实例
     * @param provider 目标运行时上下文
     * @return 目标运行时上下文
     */
    public IFunctionRuntimeProvider mergeTo(IFunctionRuntimeProvider provider){
        if (!(provider instanceof AbstractFunctionRuntimeProvider)){
            throw new IllegalArgumentException("provider must be instance of AbstractFunctionRuntimeProvider, but it is "+provider.getClass().getName());
        }
        AbstractFunctionRuntimeProvider p=(AbstractFunctionRuntimeProvider)provider;
        if (p.variablesTable==null){
            p.variablesTable=this.variablesTable;
        }else{
            p.variablesTable.readAll(this.variablesTable);
        }
        if (p.breakpoints==null){
            p.breakpoints=this.breakpoints;
        }else{
            p.breakpoints.addAll(this.breakpoints);
        }
        p.functionInfo=this.functionInfo;
        if (p.outOfBandData==null){
            p.outOfBandData=this.outOfBandData;
        }else{
            p.outOfBandData.putAll(this.outOfBandData);
        }
        // 注意,忽略了functionInfo
        return p;
    }
}
