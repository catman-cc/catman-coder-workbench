package cc.catman.workbench.api.server.websocket.run;

import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.workbench.api.server.websocket.run.debug.IDebugSession;
import cc.catman.workbench.api.server.websocket.run.value.breakpoint.BreakPointManager;

import java.util.Map;


public interface IDebugContext {

    BreakPointManager getBreakPointManager();

    IDebugSession getDebugSession();

    Map<String,Object> getVariables();

    /**
     * 等待断点的执行,如果没有断点,则立即返回
     * @param valueProvider 断点所在的值提供者
     * @param parentContext 父上下文
     * @param presetVariables   预设变量
     * @return 是否等待成功
     */
    boolean wait(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables);

    /**
     * 在断点所处的上下文评估其他值提供者
     * @param valueProviderDefinition  值提供者定义
     * @param variables 变量
     */
    void eval(ValueProviderDefinition valueProviderDefinition,Map<String,Object> variables);
}
