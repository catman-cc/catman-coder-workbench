package cc.catman.workbench.api.server.configuration.exception.constatns;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public final class ExceptionConstants {
    /**
     * 默认异常码
     */
    public static final String DEFAULT_EXCEPTION_CODE = "999";
    /**
     * 默认异常提示信息
     */
    public static final String DEFAULT_EXCEPTION_MESSAGE = "服务器内部异常";


    /**
     * 异常发生时，默认的HTTP返回状态码，默认200
     */
    public static HttpStatus GLOBAL_DEFAULT_EXCEPTION_HTTP_STATUS = HttpStatus.OK;
    /**
     * 默认业务异常码为999
     */
    public static final String GLOBAL_DEFAULT_EXCEPTION_CODE = "999";
    /**
     * 默认连接业务异常和子异常的分隔符
     */
    public static final String GLOBAL_DEFAULT_SPLIT = "-";

    /**
     * 异常默认触发的日志级别
     */
    public static final LogLevel GLOBAL_DEFAULT_EXCEPTION_LOG_LEVEL = LogLevel.WARN;
}
