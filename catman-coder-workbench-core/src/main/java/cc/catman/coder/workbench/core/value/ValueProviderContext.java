package cc.catman.coder.workbench.core.value;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.ExpressionParser;

import java.util.Map;

public interface ValueProviderContext {
    <T> T convert(Object value, TypeDescriptor type);

    /**
     * 创建表达式解析器
     * @param language 表达式语言
     * @return 表达式解析器
     */
    ExpressionParser createExpressionParser(String language);

    /**
     * 获取上下文中的变量
     * @return 变量
     */
    Map<String,Object> getVariables();
}
