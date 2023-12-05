package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core;

/**
 * 控制回填数据的类型
 */
public enum CascadeFillType {
    ALL, // 无论数据是否进行了级联操作,该数据都会填充会原字段
    CASCADED,// 只回填执行了级联操作的数据
    IGNORED, // 只回填没有进行级联操作的数据
}
