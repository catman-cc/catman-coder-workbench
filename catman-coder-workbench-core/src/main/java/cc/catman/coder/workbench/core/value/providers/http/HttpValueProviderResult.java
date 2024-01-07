package cc.catman.coder.workbench.core.value.providers.http;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpValueProviderResult {
    private int statusCode;
    private String body;
    private Map<String, List<String>> headers;
}
