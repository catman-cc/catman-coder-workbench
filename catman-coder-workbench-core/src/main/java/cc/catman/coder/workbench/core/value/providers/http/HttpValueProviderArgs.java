package cc.catman.coder.workbench.core.value.providers.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpValueProviderArgs {
    private String url;
    @Builder.Default
    private String method="GET";
    private String body;
    @Builder.Default
    private Map<String, List<String>> headers=new HashMap<>();
    @Builder.Default
    private HttpValueProviderArgsSettings settings=HttpValueProviderArgsSettings.builder().build();
}
