package cc.catman.coder.workbench.core.core.value;

import cc.catman.coder.workbench.core.core.Context;

/**
 * 最简单的值配置,用于直接提供值数据,比如,直接定义一个数据源,此处负责提供具体的值
 */
public class SimpleValue implements Value {

    private Object value;

    @Override
    public String getKind() {
        return "simple";
    }

    @Override
    public Object get(Context context) {
        return value;
    }
}
