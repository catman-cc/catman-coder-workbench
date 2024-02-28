package cc.catman.coder.workbench.core.runtime;

import cc.catman.coder.workbench.core.runtime.debug.BreakpointInformation;
import cc.catman.coder.workbench.core.type.TypeDefinition;

import java.util.List;
import java.util.Map;

public interface IFunctionInfo {
    /**
     * 获取当前函数的ID
     */
    String getId();

    /**
     * 获取当前函数的名称
     */
    String getName();

    /**
     * 获取当前函数的描述
     */
    String getKind();

    /**
     * 获取当前函数的参数定义
     */
    Map<String,TypeDefinition> getArgs();

    /**
     * 获取当前函数的返回值定义
     */
    TypeDefinition getResult();

    /**
     * 获取当前函数的异常处理器
     */
    List<Object> getExceptionHandlers();

    /**
     * 获取当前堆栈退出时,需要调用的函数
     * 如果在执行finally时,抛出了异常,将会中断finally的执行,并将异常抛出
     * 由上游堆栈处理,或者最终抛出到顶层堆栈
     */
    List<IFunctionCallInfo> getFinallyCalls();

    /**
     * 获取当前函数的内部函数调用队列,有可能为空
     * 函数调用可能会出现递归调用,因此在序列化时需要注意
     */
    List<IFunctionCallInfo> getCallQueue();

    /**
     * 获取当前函数的提供的断点信息,每一个方法都默认提供下列几个断点:
     * -1. before: 在函数执行之前触发
     * -2.  enter: 在函数进入时触发
     *  ... 此处为具体Function提供的内置断点信息 ...
     * -3. leave: 在函数离开时触发
     * -4. after: 在函数执行之后触发
     * - exception: 在函数抛出异常时触发
     *  事实上,在进行函数调用时,上述的断点信息不应被序列化传输,因为一个具体的FunctionExecution本身就持有了这些信息
     */
    List<BreakpointInformation> getBreakpointInformation();
}
