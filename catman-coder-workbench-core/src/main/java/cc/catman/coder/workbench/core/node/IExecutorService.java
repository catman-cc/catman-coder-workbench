package cc.catman.coder.workbench.core.node;

import cc.catman.coder.workbench.core.runtime.IFunctionRuntimeProvider;

import java.util.List;

/**
 * 执行器服务
 */
public interface IExecutorService {
    /**
     * 获取支持的函数类型列表,该列表主要用于初次筛选,减少RPC调用
     * @return 支持的函数类型
     */
    List<String> supportedFunctionKinds();

    /**
     * 判断是否支持某个函数,如果支持,则返回true,否则返回false,该方法通常用于进一步筛选
     * @param provider 函数运行时提供者
     * @return  是否支持
     */
    boolean supportFunction(IFunctionRuntimeProvider provider);

    int bidding(IFunctionRuntimeProvider provider);
}
