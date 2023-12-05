package cc.catman.workbench.api.server.configuration.result;

/**
 * 统一返回结果对象
 *
 * @param <T> 返回数据对象
 */
public interface Result<T> {

    /**
     * 判断请求是否成功，结果只能是{@code true}或者{@code false}
     *
     * @return 本次请求是否成功
     */
    boolean isSuccess();

    /**
     * 获取本次请求的状态码
     *
     * @return 本次请求的状态码
     */
    String getCode();

    /**
     * 判断本次请求结果集中是否包含数据,结果只能是{@code true}或者{@code false}
     *
     * @return 本次请求结果集中是否包含数据
     */
    boolean hasData();

    /**
     * 获取本次请求获取到的数据.
     * <p>
     * 当请求成功时，该方法返回真实数据对象.
     * <p>
     * 当请求失败时，该对象返回详细的错误堆栈信息。
     * <p>
     * 建议实现类在实现该方法时，针对请求失败的场景，提供不展示错误堆栈信息的操作入口。
     *
     * @return 本次请求获取到的数据
     */
    T getData();


    /**
     * 获取错误消息
     * <p>
     * 获取请求失败时返回的提示信息，通常用于前端页面展示
     *
     * @return 错误消息
     */
    String getMessage();

    /**
     * 获取本次请求服务器响应时间
     *
     * @return 本次请求服务器响应时间
     */
    long getTimestamp();
}
