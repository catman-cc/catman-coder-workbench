package cc.catman.coder.workbench.core.value.executor;

import cc.catman.coder.workbench.core.parameter.IParameterParseStrategy;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultValueProviderExecutor extends AbstractValueProviderExecutor{

    @Builder.Default
    private List<IParameterParseStrategy> parameterParseStrategies= new ArrayList<>();

    @Override
    public ValueProviderContext createValueProviderContext(ValueProvider valueProvider, Map<String, Object> variables) {
        return getDefaultValueProviderContext(variables);
    }

    @Override
    public ValueProviderContext createValueProviderContext(ValueProviderDefinition valueProviderDefinition, Map<String, Object> presetVariables) {
        return getDefaultValueProviderContext(presetVariables);
    }

    private DefaultValueProviderContext getDefaultValueProviderContext(Map<String, Object> variables) {
        return DefaultValueProviderContext.builder()
                .id(UUID.randomUUID().toString())
                .name("root-context"+this.hashCode())
                .batchId(UUID.randomUUID().toString())
                .variables(Optional.ofNullable(variables).orElseGet(HashMap::new))
                .genericConversionService(genericConversionService)
                .valueProviderRegistry(this.getValueProviderRegistry())
                .contextManager(this.getContextManager())
                .parameterParseStrategies(parameterParseStrategies)
                .valueProviderExecutor(this)
                .build();
    }
}
