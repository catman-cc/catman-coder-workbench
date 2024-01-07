package cc.catman.coder.workbench.core.value.executor;

import cc.catman.coder.workbench.core.utils.MapBuilder;
import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.report.ReportMessage;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractValueProviderExecutor implements ValueProviderExecutor {

    protected GenericConversionService genericConversionService;

    private ValueProviderRegistry valueProviderRegistry;

    private IValueProviderContextManager contextManager;

    @Override
    public Object exec(ValueProvider valueProvider) {
        return exec(valueProvider,new HashMap<>());
    }

    @Override
    public Object exec(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables) {
        // 构建当前上下文,每次调用exec方法都会创建一个新的上下文

        ValueProviderContext context = Optional.ofNullable(parentContext)
                .map(p -> p.createChildContext(valueProvider, presetVariables))
                .orElseGet(() -> createValueProviderContext(valueProvider, presetVariables));

        context.report(() -> ReportMessage.builder()
                .sourceType("executor")
                .batchId(context.getBatchId())
                .eventKind("create-context")
                .data(MapBuilder.create()
                        .add("context", context.getId())
                        .build()
                )
                .build());

        context.report(() -> ReportMessage.builder()
                .sourceType("context")
                .sourceId(context.getId())
                .batchId(context.getBatchId())
                .eventKind("pre-run-value-provider")
                .data(MapBuilder.create()
                        .add("valueProvider", valueProvider.getId())
                        .add("variables", context.variables())
                        .add("all-variables", context.buildVariables())
                        .build()
                )
                .build());

        try {
            // 获取值,并将值存储到上下文中
            Object o = valueProvider.run(context).orElse(null);
            context.report(() -> ReportMessage.builder()
                    .sourceType("context")
                    .sourceId(context.getId())
                    .batchId(context.getBatchId())
                    .eventKind("after-run-value-provider")
                    .data(MapBuilder.create()
                            .add("valueProvider", valueProvider.getId())
                            .add("result", o)
                            .build()
                    )
                    .build());
            if (Optional.ofNullable(parentContext).isPresent()) {
                context.report(() -> ReportMessage.builder()
                        .sourceType("context")
                        .sourceId(context.getId())
                        .batchId(context.getBatchId())
                        .eventKind("backfill-the-parent-context-variable")
                        .data(MapBuilder.create()
                                .add("valueProvider", valueProvider.getId())
                                .add("variable-name", valueProvider.getName())
                                .add("variable-value", o)
                                .build()
                        )
                        .build());
                parentContext.addVariable(valueProvider.getName(), o);
            }
            return o;
        } catch (Exception e) {
            context.report(() -> ReportMessage.builder()
                    .sourceType("context")
                    .sourceId(context.getId())
                    .batchId(context.getBatchId())
                    .eventKind("value-provider-exception")
                    .data(MapBuilder.create()
                            .add("valueProvider", valueProvider.getId())
                            .add("exception", e)
                            .build()
                    )
                    .build());
            throw e;
        }
    }
    @Override
    public <T> T exec(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables, Class<T> resultType) {
        return this.genericConversionService.convert(exec(valueProvider,parentContext,presetVariables),resultType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T exec(ValueProvider valueProvider, ValueProviderContext parentContext, Map<String, Object> presetVariables, TypeDescriptor resultType) {
        return (T) this.genericConversionService.convert(exec(valueProvider,parentContext,presetVariables),resultType);
    }

    @Override
    public Object exec(ValueProviderDefinition valueProviderDefinition, Map<String, Object> variables) {
        ValueProviderContext valueProviderContext = createValueProviderContext(valueProviderDefinition, variables);
        valueProviderContext.report(()->ReportMessage.builder()
                        .sourceType("executor")
                        .sourceId("")
                        .eventKind("create-context")
                        .data(MapBuilder.create()
                                .add("contextId",valueProviderContext.getId())
                                .add("contextName",valueProviderContext.getName())
                                .add("batchId",valueProviderContext.getBatchId())
                                .build()
                        )
                .build());

        return valueProviderContext.exec(valueProviderDefinition);
    }

    @Override
    public <T> T exec(ValueProviderDefinition valueProviderDefinition, Map<String, Object> variables, Class<T> resultType) {
        return exec(valueProviderDefinition,variables,TypeDescriptor.valueOf(resultType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T exec(ValueProviderDefinition valueProviderDefinition, Map<String, Object> variables, TypeDescriptor resultType) {
        return (T)this.genericConversionService.convert(exec(valueProviderDefinition,variables),resultType);
    }

}
