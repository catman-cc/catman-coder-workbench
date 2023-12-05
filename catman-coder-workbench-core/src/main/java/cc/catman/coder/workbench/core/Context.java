package cc.catman.coder.workbench.core;

/**
 * 上下文,上下文在处理任务的时候,首先要分析任务,因为有可能需要预准备一下基础数据
 * 实际上还是在最开始的时候由ValueProvider提供的值,比如说,定义了一个MysqlDataSourceProvider,负责提供mysql数据源的信息
 */
public interface Context {

}
