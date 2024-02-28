package cc.catman.coder.workbench.core.runtime.debug;

public interface DebugConstants {
    interface Parameter{

        /*
         * 开始解析参数
         */
        String BEFORE_PARSE_PARAMETER = "BEFORE_PARSE_PARAMETER";

        /**
         * 结束解析参数
         */
        String AFTER_PARSE_PARAMETER = "AFTER_PARSE_PARAMETER";
    }

    interface Function{
        /**
         * 开始执行函数
         */
        String BEFORE_EXECUTE_FUNCTION = "BEFORE_EXECUTE_FUNCTION";

        /**
         * 结束执行函数
         */
        String AFTER_EXECUTE_FUNCTION = "AFTER_EXECUTE_FUNCTION";

        /**
         * 执行函数出错
         */
        String EXECUTE_FUNCTION_ERROR = "EXECUTE_FUNCTION_ERROR";
    }
}
