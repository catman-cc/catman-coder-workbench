package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.Context;

/**
 * 值接口定义
 */
public interface Value {

    /**
     * 获取值的类型,kind主要用于反序列化操作
     * 
     * @return
     */
    String getKind();

    /**
     * 从上下文取值
     * 
     * @param context
     * @return
     */
    Object get(Context context);
}
