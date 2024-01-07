package cc.catman.coder.workbench.core.value.providers.expressions;

import cc.catman.coder.workbench.core.value.ValueProviderContext;
import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;

import java.util.Optional;

/**
 * 表达式取值
 */
@Slf4j
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExpressionsValueProvider extends AbstractValueProvider {
    @Builder.Default
    private String kind="expressions";

    private String language;

    private String expression;

    @Override
    public Optional<Object> run(ValueProviderContext context) {
        // 将数据传递给上下文,并由上下文完成表达式的解析
        ExpressionParser parser = context.createExpressionParser(language);
        EvaluationContext evaluationContext=context.createEvaluationContext(context.buildVariables());
        try {
            Object value = parser.parseExpression(expression).getValue(evaluationContext);
            return Optional.ofNullable(value);
        } catch (EvaluationException | ParseException e) {
           log.error("表达式解析失败",e);
        }
        return Optional.empty();
    }
}
