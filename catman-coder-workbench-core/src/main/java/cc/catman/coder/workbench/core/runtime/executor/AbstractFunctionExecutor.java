package cc.catman.coder.workbench.core.runtime.executor;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.runtime.*;
import cc.catman.coder.workbench.core.runtime.debug.DebugConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表达式执行器
 */
public abstract class AbstractFunctionExecutor implements IFunctionExecutor {
    @Override
    public IFunctionCallResultInfo execute(IFunctionRuntimeProvider provider, IRuntimeStack stack) {
        return execute(provider.getFunctionInfo(), stack);
    }

    @Override
    public IFunctionCallResultInfo execute(IFunctionCallInfo callInfo, IRuntimeStack stack) {
        // 解析表达式,需要一个入参: expression
        // 获取参数定义,并进行解析操作,该操作必须由具体的函数解析完成解析
        IRuntimeDebuggerContext debug = stack.getRuntimeDebuggerContext();
        debug.triggerBreakpoint(stack,DebugConstants.Parameter.BEFORE_PARSE_PARAMETER);

        Map<String, Parameter> args = callInfo.getArgs();
        // 调用堆栈,解析参数
        Object argsValues = parseArgs(callInfo, stack, args);

        debug.triggerBreakpoint(stack,DebugConstants.Parameter.AFTER_PARSE_PARAMETER);

        try {
            debug.triggerBreakpoint(stack,DebugConstants.Function.BEFORE_EXECUTE_FUNCTION);
            Object result = this.doExecute(stack, argsValues);
            debug.triggerBreakpoint(stack,DebugConstants.Function.AFTER_EXECUTE_FUNCTION);
            return DefaultFunctionCallResultInfo.of(result);
        } catch (Exception e) {
            // 发生了异常,将异常数据上报给调试器
            debug.triggerBreakpoint(stack,DebugConstants.Function.EXECUTE_FUNCTION_ERROR, e);
            stack.reportException(e);
            return DefaultFunctionCallResultInfo.error(e);
        }
    }

    /**
     * 解析参数
     *
     * @param callInfo 函数调用信息
     * @param stack    运行时栈
     * @param args     参数定义
     * @return 参数
     */
    protected Object parseArgs(IFunctionCallInfo callInfo, IRuntimeStack stack, Map<String,Parameter> args) {
        // 获取参数定义,并进行解析操作,因为每一个参数的结构都可能不一致,所以解析操作必须由具体的函数策略完成,解析策略
        IParameterParserManager parser = stack.getParameterParserManager();
        Map<String,Object> argsValues=new LinkedHashMap<>();
        args.forEach((name, param) -> {
            // 解析参数时,应创建一个子堆栈,使用子堆栈进行参数解析,避免变量污染
            IRuntimeStack childStack = stack.createChildStack("arg-" + name, callInfo);
            // 解析参数定义
            ParameterParserResult result = parser.parse(param, childStack);
            if (result.hasException()) {
                childStack.reportException(result.getException());
            }
            argsValues.put(name, result.getResult());
        });

        return doParseArgs(callInfo, stack, args,argsValues);
    }

    protected  Object doParseArgs(IFunctionCallInfo callInfo, IRuntimeStack stack, Map<String,Parameter> args,Map<String,Object> argsValues){
        return argsValues;
    }

    /**
     * 执行函数
     *
     * @param stack      运行时栈
     * @param argsValues 参数值
     * @return 执行结果
     */
    protected abstract Object doExecute(IRuntimeStack stack, Object argsValues);
}
