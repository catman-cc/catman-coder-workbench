package cc.catman.workbench.service.core.po.function;

public enum EFunctionInfoParamType {
    /**
     * 输入参数
     */
    INPUT,
    /**
     * 输出参数
     */
    OUTPUT,
    ;
    public boolean isInput(){
        return this == INPUT;
    }
    public boolean isOutput(){
        return this == OUTPUT;
    }


}
