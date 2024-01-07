package cc.catman.coder.workbench.core.value.providers;

import cc.catman.coder.workbench.core.value.ValueProviderConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DefaultValueProviderConfig implements ValueProviderConfig {
    private String id;
    @Builder.Default
    private Map<String,Object> values =new HashMap<>();
}
