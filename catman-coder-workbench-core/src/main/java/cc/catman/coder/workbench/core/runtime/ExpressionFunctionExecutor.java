package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Map;

/**
 * 表达式函数执行器,当前表达式执行器为SPeL表达式执行器
 */
public class ExpressionFunctionExecutor extends AbstractFunctionExecutor{
    private static final List<String> EXPRESSION_KEYS = List.of(".", "[", "]", "{", "}", "(", ")",
            "*", "/", "+", "-", "%", "&", "|", "^", "~", "!", "<", ">", "=", "?", ":");


    private final ExpressionParser expressionParser;

    public static ExpressionFunctionExecutor create(ExpressionParser expressionParser){
        return new ExpressionFunctionExecutor(expressionParser);
    }

    public static ExpressionFunctionExecutor create(){
        return new ExpressionFunctionExecutor(new SpelExpressionParser());
    }

    public ExpressionFunctionExecutor(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    protected Object doParseArgs(IFunctionCallInfo callInfo, IRuntimeStack stack, Map<String, Parameter> args, Map<String, Object> argsValues) {

        return argsValues;
    }

    @Override
    protected Object doExecute(IRuntimeStack stack, Object argsValues) {
        String expression = (String) argsValues;
        if(expression==null||expression.isEmpty()){
            throw new RuntimeException("表达式为空");
        }
        if (EXPRESSION_KEYS.stream().noneMatch(expression::contains)){
            // 表达式中不包含任何运算符,则表达式为一个变量名,直接获取变量的值
            return stack.getVariablesTable().getVariable(expression);
        }
        // 表达式中包含运算符,则需要解析表达式
        Map<String, Object> variables = stack.getVariablesTable().getVariables();
        return expressionParser.parseExpression(expression).getValue(variables);
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        return null;
    }
}
