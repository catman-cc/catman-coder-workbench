package cc.catman.workbench.api.server.configuration.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 抽象返回结果集
 *
 * @param <T> 数据对象类型
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbstractResult<T> implements Result<T> {
    /**
     * 请求是否成功的标志
     */
    private boolean success;
    /**
     * 本次请求的状态码
     */
    private String code;
    /**
     * 请求成功时，该对象表示实际数据对象。
     * 请求失败时，该对象表示错误堆栈信息。
     */
    private T data;

    /**
     * 错误提示信息
     */
    private String message;

    /**
     * 请求响应时间戳
     */
    private long timestamp;


    @Override
    public boolean hasData() {
        return this.data != null;
    }

    public AbstractResult(Result<T> result) {
        this.success = result.isSuccess();
        this.code = result.getCode();
        this.data = result.getData();
        this.message = result.getMessage();
        this.timestamp = result.getTimestamp();
    }
}
