package cc.catman.coder.workbench.core.value.providers.json;

import cc.catman.coder.workbench.core.value.*;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProviderFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class JsonValueProviderFactory extends AbstractValueProviderFactory {

    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    protected AbstractValueProvider create(ValueProviderDefinition valueProviderDefinition, ValueProviderRegistry valueProviderRegistry, ValueProviderContext context) {
        String json = Optional.ofNullable(context.parse(valueProviderDefinition.getArgs(), String.class))
                .orElseGet(() -> context.getVariable("__parameter__name__", String.class));
        return JsonValueProvider.builder()
                .objectMapper(objectMapper)
                .value(json)
                .build();
    }
}
