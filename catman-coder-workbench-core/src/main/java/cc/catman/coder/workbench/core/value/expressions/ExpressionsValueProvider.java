package cc.catman.coder.workbench.core.value.expressions;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.AbstractValueProvider;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Optional;

/**
 * 表达式取值
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ExpressionsValueProvider extends AbstractValueProvider<ExpressionsValueProviderConfig> {
    @Builder.Default
    private String kind="expressions";
    @Override
    public Optional<Object> get(ValueProviderContext context) {
        ExpressionsValueProviderConfig config = getConfig();
        // 将数据传递给上下文,并由上下文完成表达式的解析
        ExpressionParser parser = context.createExpressionParser(config.getLanguage());
        EvaluationContext evaluationContext=new StandardEvaluationContext(context.getVariables());
        Object value = parser.parseExpression(config.getExpression()).getValue(evaluationContext);
        return Optional.ofNullable(value);
    }
}
