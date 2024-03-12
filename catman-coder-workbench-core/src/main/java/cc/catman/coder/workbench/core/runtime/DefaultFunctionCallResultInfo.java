package cc.catman.coder.workbench.core.runtime;

import lombok.Data;

import java.util.Optional;

@Data
public class DefaultFunctionCallResultInfo implements IFunctionCallResultInfo{
    private Object result;

    private Object exception;

    private Long totalTimeSpent;

    private Long internalTimeSpend;

    public static DefaultFunctionCallResultInfo of(Object result){
        DefaultFunctionCallResultInfo info = new DefaultFunctionCallResultInfo();
        info.result = result;
        return info;
    }

    public static DefaultFunctionCallResultInfo error(Object exception){
        DefaultFunctionCallResultInfo info = new DefaultFunctionCallResultInfo();
        info.exception = exception;
        return info;
    }

    @Override
    public Optional<Object> getException() {
        return Optional.ofNullable(exception);
    }

    @Override
    public Long getTotalTimeSpent() {
        return totalTimeSpent;
    }

    @Override
    public Optional<Long> getInternalTimeSpend() {
        return Optional.ofNullable(internalTimeSpend);
    }
}
