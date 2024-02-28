package cc.catman.workbench.api.server.configuration.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import  cc.catman.workbench.api.server.configuration.exception.annotations.Code;
import  cc.catman.workbench.api.server.configuration.exception.constatns.ExceptionConstants;
import  cc.catman.workbench.api.server.configuration.result.Result;
import  cc.catman.workbench.api.server.configuration.result.UniversalResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 默认业务异常处理器
 */
public class DefaultBusinessRuntimeExceptionHandler implements BusinessRuntimeExceptionHandler {

    /**
     * 异常发生时，默认的HTTP返回状态码，默认200
     */
    @Getter
    @Setter
    protected HttpStatus globalDefaultExceptionHttpStatus = ExceptionConstants.GLOBAL_DEFAULT_EXCEPTION_HTTP_STATUS;
    /**
     * 默认业务异常码为999
     */
    @Getter
    @Setter
    protected String globalDefaultExceptionCode = ExceptionConstants.GLOBAL_DEFAULT_EXCEPTION_CODE;
    /**
     * 默认连接业务异常和子异常的分隔符
     */
    @Getter
    @Setter
    protected String globalDefaultSplit = ExceptionConstants.GLOBAL_DEFAULT_SPLIT;

    /**
     * 是否返回异常到调用方
     */
    @Getter
    @Setter
    protected boolean isReturnErrorInfo = false;

    @Override
    public Result buildResult(HttpServletRequest request, HttpServletResponse response, BusinessRuntimeException e) {
        // 获取异常数据
        Class<? extends BusinessRuntimeException> eClass = e.getClass();

        // 获取异常码
        boolean hasCode = eClass.isAnnotationPresent(Code.class);
        Code code = hasCode ? eClass.getDeclaredAnnotation(Code.class) : null;
        String errorCode = hasCode ? code.code() : getGlobalDefaultExceptionCode();
        HttpStatus responseStatus = hasCode ? code.httpStatus() : getGlobalDefaultExceptionHttpStatus();
        String split = hasCode ? code.split() : getGlobalDefaultSplit();

        // 获取子系统异常码
        String subSystemCode = e.getCode();
        // 获取完成异常码
        String finalCode = errorCode.concat(split).concat(subSystemCode);
        // 获取异常信息
        String message = e.getErrorMessage();
        UniversalResult<Throwable> result = new UniversalResult.Builder<Throwable>()
                .code(finalCode)
                .errorMessage(message)
                .build();
        // 展示具体的异常数据到前端
        if (isReturnErrorInfo()) {
            result.setData(e);
            result.setHasData(Boolean.TRUE);
        }
        // 更新响应状态
        response.setStatus(responseStatus.value());
        return result;
    }

    public static class DefaultBusinessRuntimeExceptionHandlerCallback implements BusinessRuntimeExceptionHandlerCallBack {

        private ObjectMapper objectMapper;

        public DefaultBusinessRuntimeExceptionHandlerCallback(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public void callback(HttpServletRequest request, HttpServletResponse response, BusinessRuntimeException exception, Result result) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            try {
                response.getWriter().append(covert2String(result));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        protected String covert2String(Result result) {
            try {
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
