package cc.catman.coder.workbench.core.runner;

import cc.catman.coder.workbench.core.Base;

/**
 * 用于执行函数的runner,每个runner都是一个独立的进程
 */
public class Runner extends Base {
    /**
     * runner的唯一标志
     */
    private String id;

    /**
     * runner的名称
     */
    private String name;

    /**
     * runner的类型
     */
    private String kind;

    /**
     * runner的主进程语言,比如java,python,go等
     */
    private String language;

    /**
     * runner的描述
     */
    private String describe;

    /**
     * runner的参数
     */
    private String args;
}
