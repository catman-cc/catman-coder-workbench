package cc.catman.coder.workbench.core.value.providers.samename;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.util.Optional;

/**
 * 直接在上下文环境变量中使用同名取值法
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SameNameValueProvider extends AbstractValueProvider {
    public static final String KIND = "sameName";
    private String value;
    @Override
    public Optional<Object> run(ValueProviderContext context) {
        ExpressionParser parser = context.createExpressionParser("spel");
        Expression expression = parser.parseExpression(value);

        EvaluationContext evaluationContext = context.createEvaluationContext(context.buildVariables());
        return Optional.ofNullable(expression.getValue(evaluationContext));
    }
}
