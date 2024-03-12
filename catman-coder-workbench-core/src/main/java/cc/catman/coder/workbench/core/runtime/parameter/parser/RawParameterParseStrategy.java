package cc.catman.coder.workbench.core.runtime.parameter.parser;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.IRuntimeStack;

/**
 * 原始类型参数解析策略,
 */
public class RawParameterParseStrategy extends AbstractParameterParseStrategy {
    private final Class<?> rawType;

    private final String typeName;

    public static RawParameterParseStrategy create(Class<?> rawType, String typeName) {
        return new RawParameterParseStrategy(rawType, typeName);
    }

    public RawParameterParseStrategy(Class<?> rawType, String typeName) {
        this.rawType = rawType;
        this.typeName = typeName;
    }

    @Override
    public Object doParse(Parameter parameter, Object preParseValue, IRuntimeStack stack) {
        if (preParseValue == null) {
            return null;
        }
        // 尝试进行类型转换操作
        return stack.convertTo(preParseValue, rawType);
    }

    @Override
    public String getSupportTypeName() {
        return this.typeName;
    }
}
