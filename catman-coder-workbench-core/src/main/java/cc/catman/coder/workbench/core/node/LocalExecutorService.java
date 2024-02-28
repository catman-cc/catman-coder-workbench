package cc.catman.coder.workbench.core.node;

import cc.catman.coder.workbench.core.runtime.IFunctionCallInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionInfo;
import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地执行器服务,通常来说本地执行器服务的函数列表除了固定方法之外,还会包含一些自定义方法
 * 这些方法可能是有外部数据源注入的,比如从数据库加载自定义函数
 */

public class LocalExecutorService implements IExecutorService{

    private List<String> supportedFunctionKinds;

    public LocalExecutorService() {
        this(new ArrayList<>());
    }

    public LocalExecutorService(List<String> supportedFunctionKinds) {
        this.supportedFunctionKinds = supportedFunctionKinds;
    }

    /**
     * 获取支持的函数类型列表,该列表主要用于初次筛选,减少RPC调用
     * @return 支持的函数类型
     */
    @Override
    public List<String> supportedFunctionKinds() {
        return this.supportedFunctionKinds;
    }

    @Override
    public boolean supportFunction(IFunctionRuntimeProvider provider) {
        IFunctionCallInfo callInfo = provider.getFunctionInfo();
        IFunctionInfo functionInfo = callInfo.getFunctionInfo();
        String kind= functionInfo.getKind();
        return this.supportedFunctionKinds().contains(kind);
    }

    @Override
    public int bidding(IFunctionRuntimeProvider provider) {
        return 1;
    }
}
