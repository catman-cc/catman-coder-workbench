package cc.catman.workbench.api.server.configuration.exception;

import  cc.catman.workbench.api.server.configuration.result.Result;
import lombok.Builder;
import lombok.Getter;

/**
 * 用于逃脱方法响应限制的异常信息
 *
 * @author Jpanda [Jpanda@aliyun.com]
 * @version 1.0
 * @since 2020/6/12 15:24:30
 */
@Builder
@Getter
public class EscapeRestrictedResponseRuntimeException extends RuntimeException {

    private boolean isJson = true;
    /**
     * 返回结果
     */
    private Result<Object> result;

    private String streamResult;

}
