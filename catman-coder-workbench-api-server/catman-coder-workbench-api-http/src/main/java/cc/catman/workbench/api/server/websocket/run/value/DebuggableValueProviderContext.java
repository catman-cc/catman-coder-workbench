package cc.catman.workbench.api.server.websocket.run.value;

import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.GroupedMessageQueue;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.parameter.DefaultParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseHandlerContext;
import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.report.ReportMessage;
import cc.catman.workbench.api.server.websocket.run.debug.IDebugSession;
import cc.catman.workbench.api.server.websocket.run.value.breakpoint.BreakPointManager;
import lombok.*;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 调试用的值提供者上下文,该上下文接受外部控制,用于调试
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebuggableValueProviderContext implements ValueProviderContext {
    private String id;
    private String name;
    private String batchId;
    private ValueProviderContext proxy;
    private IDebugSession session;
    private MessageChannel messageChannel;
    private BreakPointManager breakPointManager;
    protected ValueProviderContext parentContext;
    private ValueProviderExecutor valueProviderExecutor;
    private GroupedMessageQueue<Message<?>> messageQueue;

    @Override
    public void report(ReportMessage<?> message) {
        Message<ReportMessage<?>> msg = Message.of(message, messageChannel);
        msg.setTopic("context-report");
        messageQueue.put(messageChannel.getConnection().getId(), msg);
    }

    @Override
    public void report(Supplier<ReportMessage<?>> messageSupplier) {
        report(messageSupplier.get());
    }

    @Override
    public ValueProviderContext createChildContext(ValueProvider valueProvider, Map<String, Object> variables) {
        ValueProviderContext childContext = proxy.createChildContext(valueProvider, variables);
        String namePrefix = Optional.ofNullable(valueProvider).map(ValueProvider::getName).orElseGet(() -> this.getName().substring(0, 12) + "-child");
        return DebuggableValueProviderContext.builder()
                .id(UUID.randomUUID().toString())
                .name(namePrefix + "-context" + this.id)
                .batchId(this.batchId)
                .parentContext(this)
                .proxy(childContext)
                .valueProviderExecutor(this.getValueProviderExecutor())
                .messageChannel(messageChannel)
                .messageQueue(messageQueue)
                .build();
    }

    @Override
    public IParameterParseHandlerContext createParameterParseHandlerContext() {
        return DebuggableParameterParseHandlerContext.builder()
                .proxy(DefaultParameterParseHandlerContext.builder()
                        .parameterParseStrategies(proxy.getParameterParseStrategies())
                        .valueProviderContext(this).build())
                .build();
    }

    @Override
    public ValueProviderExecutor getValueProviderExecutor() {
        return this.valueProviderExecutor;
    }

    @Override
    public EvaluationContext createEvaluationContext(Map<String, Object> variables) {
        return null;
    }

    @Override
    public Object getVariable(String name) {
        return proxy.getVariable(name);
    }

    @Override
    public Map<String, Object> variables() {
        return proxy.variables();
    }

    @Override
    public Map<String, Object> buildVariables() {
        return proxy.buildVariables();
    }

    @Override
    public Map<String, Object> getVariables() {
        return proxy.getVariables();
    }

    @Override
    public void addVariable(String name, Object value) {
        proxy.addVariable(name, value);
    }

    @Override
    public Optional<ValueProviderContext> getParentContext() {
        return Optional.ofNullable(parentContext);
    }

    @Override
    public IValueProviderContextManager getContextManager() {
        return proxy.getContextManager();
    }

    @Override
    public ValueProviderRegistry getValueProviderRegistry() {
        return proxy.getValueProviderRegistry();
    }

    @Override
    public List<IParameterParseStrategy> getParameterParseStrategies() {
        return proxy.getParameterParseStrategies();
    }

    @Override
    public <T> T convert(Object value, TypeDescriptor type) {
        return proxy.convert(value, type);
    }

    @Override
    public ExpressionParser createExpressionParser(String language) {
        return proxy.createExpressionParser(language);
    }


    @Override
    public GenericConversionService conversionService() {
        return proxy.conversionService();
    }


    @Override
    public <T> T parse(Parameter parameter, TypeDescriptor descriptor) {
        return proxy.parse(parameter, descriptor);
    }

    @Override
    public ValueProvider parse(ValueProviderDefinition valueProviderDefinition, ValueProviderContext context, Map<String, Object> presetVariables) {
        return proxy.parse(valueProviderDefinition, context, presetVariables);
    }

}
