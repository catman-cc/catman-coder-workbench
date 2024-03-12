package cc.catman.coder.workbench.core.value.providers.http;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
