package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.debug.Breakpoint;

import java.util.List;
import java.util.Map;

/**
 * 函数调用信息
 * 一个函数调用信息,包含了函数的参数,返回值,异常等信息
 * 提供一个栈变量表,其中参数被分为:
 * - 全局参数,该参数将会被传递给所有的函数,包括在返回后其祖先函数的兄弟函数依然可以访问
 * - 局部参数,该参数只会被传递给当前函数及其子函数,在返回后将会被销毁
 * - 临时函数,该参数只会被传递给当前函数,在返回后将会被销毁
 * 除此之外,每个级别的参数都支持一个参数别名表,用于在函数调用时将参数名映射为其他名称,其作用是为了解决参数名冲突的问题
 * 提供当前调用的函数信息
 */
public interface IFunctionCallInfo {

    /**
     * 获取当前函数的参数定义
     */
    Map<String,Parameter> getArgs();

    /**
     * 获取当前函数的返回值定义
     */
    Parameter getResult();

    /**
     * 重写函数返回值变量表时,通过该方法获取变量名称,默认值为function name
     */
    String resultName();

    /**
     * 获取当前函数的异常处理器
     */
    List<IFunctionCallExceptionHandler> getExceptionHandlers();


    /**
     * 获取当前函数的断点信息
     * @return 断点信息
     */
    List<Breakpoint> getBreakpoints();

    IFunctionInfo getFunctionInfo();

    Object call(IRuntimeStack stack);
}
