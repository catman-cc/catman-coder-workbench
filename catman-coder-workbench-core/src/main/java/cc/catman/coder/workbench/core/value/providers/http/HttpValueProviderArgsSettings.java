package cc.catman.coder.workbench.core.value.providers.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpValueProviderArgsSettings {
    @Builder.Default
    private long timeout = 5000;
}
