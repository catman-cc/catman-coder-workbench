package cc.catman.coder.workbench.core.schedule;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;

/**
 * 本地值提供者执行器调度器,一个最简单的调度器,直接返回一个执行器
 */
public class LocalSimpleValueProviderExecutorSchedule implements IValueProviderExecutorSchedule{

    private ValueProviderExecutor executor;
    @Override
    public ValueProviderExecutor schedule(ValueProviderDefinition valueProviderDefinition) {
        return executor;
    }

    @Override
    public ValueProviderExecutor schedule(ValueProviderDefinition valueProviderDefinition, ValueProviderContext parentContext) {
        return executor;
    }
}
