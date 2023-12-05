package cc.catman.workbench.api.server.configuration.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 普遍通用的一个返回结果对象
 */
@NoArgsConstructor
@AllArgsConstructor
public class UniversalResult<T> extends AbstractResult<T> {
    /**
     * 是否包含数据对象
     */
    @Setter
    private boolean hasData;

    @Override
    public boolean hasData() {
        return hasData;
    }

    public UniversalResult(Result<T> result) {
        super(result);
        hasData = result.getData() != null;
    }

    public UniversalResult(UniversalResult<T> result) {
        super(result);
        hasData = result.hasData;
    }

    public UniversalResult copy() {
        return new UniversalResult<T>(this);
    }


    public static class Builder<T> {

        private UniversalResult<T> result = new UniversalResult<>();

        public Builder<T> success(Boolean success) {
            result.setSuccess(success);
            return this;
        }

        public Builder<T> code(String code) {
            result.setCode(code);
            return this;
        }

        public Builder<T> data(T data) {
            result.setData(data);
            return this;
        }

        public Builder<T> errorMessage(String errorMessage) {
            result.setMessage(errorMessage);
            return this;
        }

        public Builder<T> timestamp(Long timestamp) {
            result.setTimestamp(timestamp);
            return this;
        }

        public Builder<T> hasData(boolean hasData) {
            result.setHasData(hasData);
            return this;
        }

        public UniversalResult<T> build() {
            // 校验成功标志
            // 校验请求状态码
            if (StringUtils.isEmpty(result.getCode())) {
                result.setCode(ResultUtils.DEFAULT_CODE);
            }
            // 校验数据
            // 校验错误提示信息
            // 校验响应时间戳
            // 校验是否有数据参数
            // 返回数据对象
            return result;
        }

    }
}
