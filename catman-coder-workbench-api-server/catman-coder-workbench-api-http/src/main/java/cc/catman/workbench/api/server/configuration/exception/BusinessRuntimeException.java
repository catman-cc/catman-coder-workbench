package cc.catman.workbench.api.server.configuration.exception;

import  cc.catman.workbench.api.server.configuration.exception.annotations.Code;
import  cc.catman.workbench.api.server.configuration.exception.annotations.Prefix;
import  cc.catman.workbench.api.server.configuration.exception.constatns.ExceptionConstants;
import  cc.catman.workbench.api.server.configuration.exception.ecodes.ECode;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 调补代课管理系统基础异常定义
 */
@Code(httpStatus = HttpStatus.OK, code = "EXCEPTION", log = LogLevel.WARN)
@Getter
public class BusinessRuntimeException extends RuntimeException {
    /**
     * 业务异常码
     */
    private String code;
    /**
     * 业务异常原始信息
     */
    private String originalMessage;
    /**
     * 业务异常完整信息
     */
    private String errorMessage;

    /**
     * 用于格式异常信息的参数
     */
    private List<Serializable> params;

    private BusinessRuntimeException(String errorMessage) {
        super(errorMessage);
        this.code = ExceptionConstants.DEFAULT_EXCEPTION_CODE;
        this.errorMessage = ExceptionConstants.DEFAULT_EXCEPTION_MESSAGE;
        this.originalMessage = this.errorMessage;
        this.params = Collections.emptyList();
    }

    private BusinessRuntimeException(String code, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
        this.originalMessage = errorMessage;
    }

    protected BusinessRuntimeException(String code, String originalMessage, String errorMessage) {
        super(errorMessage);
        this.code = code;
        this.originalMessage = originalMessage;
        this.errorMessage = errorMessage;
        this.params = Collections.emptyList();
    }

    protected BusinessRuntimeException(String code, String originalMessage, String errorMessage, List<Serializable> params) {
        super(errorMessage);
        this.code = code;
        this.originalMessage = originalMessage;
        this.errorMessage = errorMessage;
        this.params = params;
    }

    protected BusinessRuntimeException(String code, String originalMessage, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.code = code;
        this.originalMessage = originalMessage;
        this.errorMessage = errorMessage;
        this.params = Collections.emptyList();
    }

    protected BusinessRuntimeException(String code, String originalMessage, String errorMessage, List<Serializable> params, Throwable cause) {
        super(errorMessage, cause);
        this.code = code;
        this.originalMessage = originalMessage;
        this.errorMessage = errorMessage;
        this.params = params;
    }

    protected BusinessRuntimeException(String code, String originalMessage, String errorMessage, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorMessage, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.originalMessage = originalMessage;
        this.errorMessage = errorMessage;
        this.params = Collections.emptyList();
    }

    protected BusinessRuntimeException(String code, String originalMessage, String errorMessage, List<Serializable> params, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorMessage, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.originalMessage = originalMessage;
        this.errorMessage = errorMessage;
        this.params = params;
    }


    protected BusinessRuntimeException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    protected BusinessRuntimeException(Throwable cause) {
        super(cause);
    }


    public static BusinessRuntimeException exception(ECode<String> eCode) {
        return new BusinessRuntimeException(joinCode(eCode), eCode.getMessage());
    }

    public static BusinessRuntimeException exception(ECode<String> eCode, Serializable... params) {
        String errorMessage = MessageFormatter.arrayFormat(eCode.getMessage(), params).getMessage();
        return new BusinessRuntimeException(joinCode(eCode), eCode.getMessage(), errorMessage);
    }

    public static BusinessRuntimeException exception(ECode<String> eCode, Throwable throwable) {
        return new BusinessRuntimeException(joinCode(eCode), eCode.getMessage(), eCode.getMessage(), throwable);
    }

    public static BusinessRuntimeException exception(ECode<String> eCode, Throwable throwable, Serializable... params) {

        String errorMessage = MessageFormatter.arrayFormat(eCode.getMessage(), params).getMessage();
        return new BusinessRuntimeException(joinCode(eCode), eCode.getMessage(), errorMessage, Arrays.asList(params), throwable);
    }

    protected static String joinCode(ECode eCode) {
        Prefix prefix = AnnotationUtils.findAnnotation(eCode.getClass(), Prefix.class);
        String eCodeName = eCode.getClass().getSimpleName().toUpperCase();

        boolean hasPrefix = prefix != null;
        String name = hasPrefix ? prefix.value() : eCodeName.endsWith("ECODE") ? eCodeName.substring(0, eCodeName.length() - 5) : eCodeName.endsWith("CODE") ? eCodeName.substring(0, eCodeName.length() - 4) : eCodeName;
        String split = hasPrefix ? prefix.split() : "-";
        return name + split + eCode.getCode();
    }
}
