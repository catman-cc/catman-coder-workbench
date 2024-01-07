package cc.catman.workbench.api.server.websocket.run.value;

import cc.catman.coder.workbench.core.message.GroupedMessageQueue;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContext;
import cc.catman.coder.workbench.core.value.executor.AbstractValueProviderExecutor;
import cc.catman.workbench.api.server.websocket.run.IDebugContext;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

/**
 * 调试用的值提供者执行器,该执行器接受外部控制,用于调试
 * 该执行器不是线程安全的,且不应该用于生产环境
 * 同时,该执行器不应该被注册到值提供者执行器调度器中,仅可由调用方显式作为其它执行器的代理使用
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DebuggableValueProviderExecutor extends AbstractValueProviderExecutor {

    private IDebugContext debugContext;

    private MessageChannel messageChannel;
    private GroupedMessageQueue<Message<?>> messageQueue;
    @Builder.Default
    private List<IParameterParseStrategy> parameterParseStrategies= new ArrayList<>();


    @Override
    public ValueProviderContext createValueProviderContext(ValueProvider valueProvider, Map<String, Object> presetVariables) {
        return getDefaultValueProviderContext(presetVariables);
    }

    @Override
    public ValueProviderContext createValueProviderContext(ValueProviderDefinition valueProviderDefinition, Map<String, Object> presetVariables) {
        return getDefaultValueProviderContext(presetVariables);
    }

    private ValueProviderContext getDefaultValueProviderContext(Map<String, Object> variables) {
        return DebuggableValueProviderContext.builder()
                .id(UUID.randomUUID().toString())
                .name("debug-root-context"+this.hashCode())
                .batchId(UUID.randomUUID().toString())
                .proxy(DefaultValueProviderContext.builder()
                        .id(UUID.randomUUID().toString())
                        .name("root-context"+this.hashCode())
                        .batchId(UUID.randomUUID().toString())
                        .variables(Optional.ofNullable(variables).orElseGet(HashMap::new))
                        .genericConversionService(genericConversionService)
                        .valueProviderRegistry(this.getValueProviderRegistry())
                        .contextManager(this.getContextManager())
                        .parameterParseStrategies(parameterParseStrategies)
                        .valueProviderExecutor(this)
                        .build())
                .valueProviderExecutor(this)
                .messageQueue(messageQueue)
                .messageChannel(messageChannel)
                .build();
    }

    @Override
    public Object exec(ValueProvider valueProvider, ValueProviderContext parentContext) {
//        debugContext.wait(valueProvider,parentContext,parentContext.buildVariables());
        return super.exec(valueProvider, parentContext);
    }
}
