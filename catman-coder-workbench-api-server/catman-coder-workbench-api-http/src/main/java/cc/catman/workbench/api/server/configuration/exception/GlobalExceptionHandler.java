package cc.catman.workbench.api.server.configuration.exception;

import cc.catman.workbench.api.server.configuration.exception.annotations.Code;
import cc.catman.workbench.api.server.configuration.exception.constatns.ExceptionConstants;
import cc.catman.workbench.api.server.configuration.exception.ecodes.GlobalECode;
import cc.catman.workbench.api.server.configuration.exception.ecodes.ValidECode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * 全局异常统一处理器
 */
@Slf4j
@RestControllerAdvice
@Import(JPandaExceptionConfigProperties.class)
public class GlobalExceptionHandler implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    @Lazy
    private BusinessRuntimeExceptionHandler runtimeExceptionHandler;


    /**
     * 业务异常拦截处理器
     * 业务级别的异常表示可预期异常，因此打印warn日志
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     * @return 返回对象
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessRuntimeException.class)
    public Object businessRuntimeExceptionHandler(HttpServletRequest request, HttpServletResponse response, BusinessRuntimeException e) {
        // 记录异常数据,可预期的异常，将会被认为是可接受的，因此默认为警告级别
        Class<? extends BusinessRuntimeException> exception = e.getClass();
        Code code = AnnotationUtils.findAnnotation(exception, Code.class);
        boolean hasCode = code != null;
        LogLevel logLevel = hasCode ? code.log() : ExceptionConstants.GLOBAL_DEFAULT_EXCEPTION_LOG_LEVEL;
        switch (logLevel) {
            case OFF: {
                break;
            }
            case TRACE: {
                if (log.isTraceEnabled()) {
                    log.trace(e.getErrorMessage(), e);
                }
                break;
            }
            case DEBUG: {
                if (log.isDebugEnabled()) {
                    log.debug(e.getErrorMessage(), e);
                }
                break;
            }
            case INFO: {
                if (log.isInfoEnabled()) {
                    log.info(e.getErrorMessage(), e);
                }
                break;
            }
            case WARN: {
                if (log.isWarnEnabled()) {
                    log.warn(e.getErrorMessage(), e);
                }
                break;
            }
            case ERROR:
            case FATAL: {
                if (log.isErrorEnabled()) {
                    log.error(e.getErrorMessage(), e);
                }
                break;
            }
            default: {

            }
        }
        return businessRuntimeExceptionHandlerImpl(request, response, e);

    }


    /**
     * 方法参数无法通过Valid校验
     *
     * @param request   请求
     * @param response  响应
     * @param exception 异常
     * @return 返回对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validationBodyException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {


        BindingResult bindingResult = exception.getBindingResult();

        if (bindingResult.hasGlobalErrors()) {
            // 优先处理对象异常
            ObjectError globalError = bindingResult.getGlobalError();
            String name = Objects.requireNonNull(globalError).getObjectName();
            String message = globalError.getDefaultMessage();
            // 转换为业务异常抛出
            //进行统一异常处理
            return businessRuntimeExceptionHandlerImpl(request, response, BusinessRuntimeException.exception(GlobalECode.INVALID_PARAMETER, exception, name, message));
        }


        // 其次处理方法异常
        // 方法参数未通过校验异常处理
        FieldError fieldError = bindingResult.getFieldError();

        // 异常信息
        String message = Objects.requireNonNull(fieldError).getDefaultMessage();
        // 获取参数名称
        String paramName = fieldError.getField();
        //进行统一异常处理
        return businessRuntimeExceptionHandlerImpl(request, response, BusinessRuntimeException.exception(ValidECode.with(exception.getBindingResult().getFieldError().getCode()), exception, paramName, message));

    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public Object httpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageConversionException exception) {
        // 必填项未填，包装异常
        String message = exception.getMessage();
        return businessRuntimeExceptionHandlerImpl(request, response, BusinessRuntimeException.exception(GlobalECode.INVALID_FORMAT, exception, message));

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object httpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException exception) {
        // 必填项未填，包装异常
        String currentMethod = exception.getMethod();
        String[] supportMethods = exception.getSupportedMethods();

        return businessRuntimeExceptionHandlerImpl(request, response, BusinessRuntimeException.exception(GlobalECode.REQUEST_METHOD_NOT_SUPPORT, exception, currentMethod, supportMethods));
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public Object handlerAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
//        if (isAjax(request)) {
//            // AJAX请求
//            return businessRuntimeExceptionHandlerImpl(request, response, BusinessRuntimeException.exception(AuthenticationECode.NO_AUTHENTICATION));
//        }
//        // 普通请求
//        return new ModelAndView("/error/403");
//    }


    @ExceptionHandler(EscapeRestrictedResponseRuntimeException.class)
    public Object handlerEscapeRestrictedResponseRuntimeException(HttpServletRequest request, HttpServletResponse response, EscapeRestrictedResponseRuntimeException exception) throws IOException {
        if (exception.isJson()) {
            return exception.getResult();
        }
        response.getWriter().write(exception.getStreamResult());
        return null;
    }

    /**
     * 标准的Exception异常
     * 预期外的异常，输出error日志
     *
     * @param request  请求
     * @param response 响应
     * @param e        异常
     * @return 返回对象
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)

    public Object ExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {

        // 未知异常，被认为是预期之外的，需要触发error日志
        if (log.isErrorEnabled()) {
            log.error("{}:{}", e.getMessage(), e);
        }
        // 获取异常数据
        // 包装异常，作为业务异常返回
        BusinessRuntimeException businessRuntimeException = BusinessRuntimeException.exception(GlobalECode.UNKNOWN_EXCEPTION_WITH_MESSAGE, e, e.getMessage());
        return businessRuntimeExceptionHandler(request, response, businessRuntimeException);
    }

    protected Object businessRuntimeExceptionHandlerImpl(HttpServletRequest request, HttpServletResponse response, BusinessRuntimeException e) {
        return runtimeExceptionHandler.buildResult(request, response, e);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 考虑根据不同 环境限制是否回显报错数据
        // nothing
    }

    @Bean
    public BusinessRuntimeExceptionHandler businessRuntimeExceptionHandler(JPandaExceptionConfigProperties configProperties) {
        DefaultBusinessRuntimeExceptionHandler defaultBusinessRuntimeExceptionHandler = new DefaultBusinessRuntimeExceptionHandler();
        defaultBusinessRuntimeExceptionHandler.setReturnErrorInfo(configProperties.isReturnErrorInfo());
        return defaultBusinessRuntimeExceptionHandler;
    }

    @Bean
    public BusinessRuntimeExceptionHandler.BusinessRuntimeExceptionHandlerCallBack businessRuntimeExceptionHandlerCallBack(ObjectMapper objectMapper) {
        return new DefaultBusinessRuntimeExceptionHandler.DefaultBusinessRuntimeExceptionHandlerCallback(objectMapper);
    }

    protected boolean isAjax(HttpServletRequest request) {

        return (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }
}
