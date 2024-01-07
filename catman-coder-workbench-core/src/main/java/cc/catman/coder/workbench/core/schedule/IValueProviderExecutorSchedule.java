package cc.catman.coder.workbench.core.schedule;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;

/**
 * 值提供者执行器调度器,通过调度器可以实现对值提供者的调度,比如可以实现对值提供者的负载均衡
 * - 调度器应该是线程安全的/无状态的/可重入的/可重用的,因为调度器可能会被多个线程同时调用
 * - 调度器应该是无感知的,即调度器不应该知道值提供者的具体实现,只需要知道值提供者的定义即可
 * - 调度器应该是可配置的,即调度器应该可以根据配置进行调度,比如可以根据配置进行负载均衡
 * 调度器在无感知的前提下,需要自行处理跨系统/跨网络的调用,比如需要处理网络链接/网络超时/网络异常等
 */
public interface IValueProviderExecutorSchedule {

    ValueProviderExecutor schedule(ValueProviderDefinition valueProviderDefinition);

    ValueProviderExecutor schedule(ValueProviderDefinition valueProviderDefinition, ValueProviderContext parentContext);

}
