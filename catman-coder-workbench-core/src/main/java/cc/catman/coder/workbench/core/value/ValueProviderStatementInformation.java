package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.parameter.Parameter;

public interface ValueProviderStatementInformation {


    /**
     * 提供 ValueProvider的名称
     */
    String name();

    /**
     * 提供 ValueProvider的唯一类型标志
     */
    String getKind();

    /**
     * 提供 ValueProvider的描述信息
     */
    String description();

    /**
     * 提供 ValueProvider的创建工厂
     */
    ValueProviderFactory getFactory();

    /**
     * 提供 ValueProvider的入参类型定义
     */
    Parameter args();

    /**
     * 提供 ValueProvider的返回值类型定义
     */
    Parameter result();
    // 后续在增加一些属于ValueProvider的独特属性,配置,限制等等
}
