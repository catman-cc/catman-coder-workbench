package cc.catman.coder.workbench.core.apis.configurations.result;

import java.util.Date;

/**
 * UniversalResult构造助手
 */
public class UniversalResultHelper {

    /**
     * 构建通用返回结果集对象
     *
     * @param <T> 返回结果类型
     * @return 返回结果对象
     */
    public static <T extends Throwable> UniversalResult<T> exception(
            T throwable
    ) {
        return exception(ResultUtils.DEFAULT_FAIL_CODE, throwable);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param errorMessage 错误信息
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T extends Throwable> UniversalResult<T> exception(
            T throwable
            , String errorMessage
    ) {
        return exception(ResultUtils.DEFAULT_FAIL_CODE, throwable, errorMessage);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param code 请求状态码
     * @param <T>  返回结果类型
     * @return 返回结果对象
     */
    public static <T extends Throwable> UniversalResult<T> exception(
            String code
            , T throwable
    ) {
        return exception(code, throwable, "");
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param code         请求状态码
     * @param errorMessage 错误信息
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T extends Throwable> UniversalResult<T> exception(
            String code
            , T throwable
            , String errorMessage
    ) {
        return fail(code, throwable, errorMessage);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param <T> 返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> fail() {
        return fail("");
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param errorMessage 错误信息
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> fail(String errorMessage) {
        return fail(ResultUtils.DEFAULT_FAIL_CODE, errorMessage);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param code         请求状态码
     * @param errorMessage 错误信息
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> fail(
            String code
            , String errorMessage
    ) {
        return fail(code, null, errorMessage);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param code         请求状态码
     * @param data         请求数据/异常数据
     * @param errorMessage 错误信息
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> fail(
            String code
            , T data
            , String errorMessage
    ) {
        return build(Boolean.FALSE, code, data, errorMessage);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param <T> 返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> success() {
        return build(Boolean.TRUE, ResultUtils.DEFAULT_CODE, null, "");
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param data 请求数据/异常数据
     * @param <T>  返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> success(T data) {
        return build(Boolean.TRUE, ResultUtils.DEFAULT_CODE, data, "");
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param code 请求状态码
     * @param data 请求数据/异常数据
     * @param <T>  返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> success(
            String code
            , T data
    ) {
        return build(Boolean.TRUE, code, data, "");
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param success 请求是否成功
     * @param code    请求状态码
     * @param data    请求数据/异常数据
     * @param <T>     返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> build(
            Boolean success
            , String code
            , T data
    ) {
        return build(success, code, data, "");

    }

    /**
     * 构建通用返回结果集对象
     *
     * @param success      请求是否成功
     * @param code         请求状态码
     * @param data         请求数据/异常数据
     * @param errorMessage 错误信息
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> build(
            Boolean success
            , String code
            , T data
            , String errorMessage
    ) {
        return build(success, code, data, errorMessage, null != data);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param success      请求是否成功
     * @param code         请求状态码
     * @param data         请求数据/异常数据
     * @param errorMessage 错误信息
     * @param hasData      是否有返回结果
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> build(
            Boolean success
            , String code
            , T data
            , String errorMessage
            , boolean hasData
    ) {
        return build(success, code, data, errorMessage, new Date().getTime(), hasData);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param success      请求是否成功
     * @param code         请求状态码
     * @param data         请求数据/异常数据
     * @param errorMessage 错误信息
     * @param timestamp    响应时间戳
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> build(
            Boolean success
            , String code
            , T data
            , String errorMessage
            , long timestamp
    ) {
        return build(success, code, data, errorMessage, timestamp, null != data);
    }

    /**
     * 构建通用返回结果集对象
     *
     * @param success      请求是否成功
     * @param code         请求状态码
     * @param data         请求数据/异常数据
     * @param errorMessage 错误信息
     * @param timestamp    响应时间戳
     * @param hasData      是否有返回结果
     * @param <T>          返回结果类型
     * @return 返回结果对象
     */
    public static <T> UniversalResult<T> build(
            Boolean success
            , String code
            , T data
            , String errorMessage
            , long timestamp
            , boolean hasData
    ) {
        return new UniversalResult.Builder<T>()
                .success(success)
                .code(code)
                .data(data)
                .errorMessage(errorMessage)
                .timestamp(timestamp)
                .hasData(hasData)
                .build();
    }
}
