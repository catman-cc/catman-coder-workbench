package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.node.IWorker;
import cc.catman.coder.workbench.core.runtime.*;
import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutor;
import cc.catman.coder.workbench.core.runtime.executor.IFunctionExecutorManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 一个节点执行器实现,除了本地执行器外,执行器通常是一个RMI服务
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerExecutor implements IExecutor{
    private String id;

    private IWorker worker;

    @Builder.Default
    private ExecutorState state=ExecutorState.PREPARE;

    /**
     *  当前执行器支持的函数信息.
     */
    @Builder.Default
    private List<IFunctionInfo> supportedFunctions=new ArrayList<>();

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public ExecutorState updateState(ExecutorState state) {
        return this.state=state;
    }


    @Override
    public int bidding(IFunctionRuntimeProvider provider) {
        List<IFunctionInfo> supportedFunctions = this.supportedFunctions;
        // 检查是否有强绑定的相关数据,如果有,则直接返回MaxValue
        Map<String, Object> outOfBandData = provider.getOutOfBandData();
        // 强绑定信息,通常和调用上下文相关,比如,多次复用同一个数据库连接,或者多次复用同一个文件句柄
        // 填充强绑定信息的操作,由调用者完成
        if(outOfBandData.containsKey("BINDING-NODE-ID")){
            if (outOfBandData.get("BINDING").equals(getId())){
                return Integer.MAX_VALUE;
            }
        }
        return supportedFunctions.stream().anyMatch(f -> {
            IFunctionCallInfo callInfo = provider.getFunctionInfo();
            IFunctionInfo functionInfo = callInfo.getFunctionInfo();
            return f.getKind().equals(functionInfo.getKind());
        })?100:-1;
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        IRuntimeStackDistributor d = stack.getDistributor();
        // 获取函数执行器
        IFunctionExecutorManager executorManager = d.getExecutorManager();
        IFunctionExecutor executor = executorManager.getExecutor(provider);
        return executor.execute(provider, stack);
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStackDistributor stackDistributor) {
        IRuntimeStack stack = stackDistributor.createRuntimeStack(provider);
        return execute(provider,stack);
    }
}
