package cc.catman.workbench.api.server.configuration.exception.ecodes;

/**
 * 动态异常码，不建议使用。
 */
public class DynamicECode implements ECode<String> {
    /**
     * 子异常码
     */
    private String code;

    /**
     * 子异常信息
     */
    private String message;


    public static DynamicECode of(String code, String message) {
        return new DynamicECode(code, message);
    }

    public DynamicECode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }


}
