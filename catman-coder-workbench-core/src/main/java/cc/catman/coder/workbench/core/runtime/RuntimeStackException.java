package cc.catman.coder.workbench.core.runtime;


import lombok.Getter;
@Getter
public class RuntimeStackException extends RuntimeException{

    private Object error;

    private IRuntimeStack stack;

    public RuntimeStackException(Object error) {
        this.error = error;
    }

    public RuntimeStackException(Object error, IRuntimeStack stack) {
        this.error = error;
        this.stack = stack;
    }

    public static RuntimeStackException of(Object error){
        return new RuntimeStackException(error);
    }

    public static RuntimeStackException of(Object error, IRuntimeStack stack){
        return new RuntimeStackException(error, stack);
    }
}
