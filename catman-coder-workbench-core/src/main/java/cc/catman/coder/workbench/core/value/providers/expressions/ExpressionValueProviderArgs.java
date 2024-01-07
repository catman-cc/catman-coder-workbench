package cc.catman.coder.workbench.core.value.providers.expressions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpressionValueProviderArgs {
    private String language;

    private String expression;
}
