package cc.catman.workbench.service.core.po.function.caller;

public enum EFunctionCallType {
    QUEUE,
    FINAL,
    ;
    public boolean isQueue(){
        return this == QUEUE;
    }
    public boolean isFinal(){
        return this == FINAL;
    }
}
