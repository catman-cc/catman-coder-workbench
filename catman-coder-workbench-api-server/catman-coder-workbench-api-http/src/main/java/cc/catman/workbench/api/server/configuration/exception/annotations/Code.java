package cc.catman.workbench.api.server.configuration.exception.annotations;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

/**
 * 基础异常码，用于修饰{BusinessRuntimeException}及其实现类
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Code {

    /**
     * 该异常将会触发的HTTP响应状态码
     */
    HttpStatus httpStatus() default HttpStatus.OK;

    /**
     * 该异常对应的业务异常码
     */
    String code() default "999";

    /**
     * 连接子异常码的分隔符
     */
    String split() default "-";

    /**
     * 触发打印日志级别
     */
    LogLevel log() default LogLevel.WARN;
}
