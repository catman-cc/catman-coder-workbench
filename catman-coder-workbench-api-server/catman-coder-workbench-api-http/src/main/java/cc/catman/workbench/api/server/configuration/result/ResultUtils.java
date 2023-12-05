package cc.catman.workbench.api.server.configuration.result;

import java.util.Arrays;
import java.util.List;

public class ResultUtils {
    /**
     * 标志成功的返回状态码集合
     */
    private final static List<Integer> SUCCESS_CODE = Arrays.asList(200, 202, 204);

    /**
     * 默认返回状态码
     */
    public static final String DEFAULT_CODE = "200";
    /**
     * 默认成功状态码
     */
    public static final String DEFAULT_SUCCESS_CODE = "200";
    /**
     * 默认失败状态码
     */
    public static final String DEFAULT_FAIL_CODE = "500";

    /**
     * 验证指定的状态码是否为成功
     *
     * @param code 响应状态码
     * @return 是否成功响应
     */
    public static Boolean isSuccess(Integer code) {
        return SUCCESS_CODE.contains(code);
    }
}
