package cc.catman.workbench.api.server.configuration.exception.ecodes;


import cc.catman.workbench.api.server.configuration.exception.annotations.Prefix;

/**
 * 全局异常码
 * !! 请尽可能返回详细的异常码
 * <p>
 * - 在返回异常码时，尽可能使用{@link #SYSTEM_EXCEPTION}取代{@link #UNKNOWN_EXCEPTION}
 * <p>
 * 参数{@link #description}仅仅只是用来描述枚举状态码的作用。
 */
@Prefix("GLOBAL")
public enum GlobalECode implements ECode<String> {

    SYSTEM_EXCEPTION("001", "系统繁忙，请稍后再试", "系统级别的业务异常，在系统发生不可预期的异常时，且无更精确的异常码可使用，可以考虑使用该异常码."),
    UNKNOWN_EXCEPTION("002", "未知异常,请重试", "参考001,在使用时尽可能使用001代替该异常码."),

    MISSING_PARAMETER("003", "缺少[{}]参数", "未传入必要的参数,类似于004"),

    PARAMETER_CAN_NOT_BE_NULL("004", "参数[{}]为必填项,不得为空", "参考003"),

    INVALID_FORMAT("005", "无效的数据格式,请检查：[{}]", "比如JSON或者XML格式错误"),

    INVALID_TIMESTAMP("006", "无效的时间戳格式[{}],请检查格式需要为:[{}]", "时间格式错误"),

    INVALID_PARAMETER("007", "无效参数[{}]：{}", "参数不能满足校验条件"),

    UNKNOWN_EXCEPTION_WITH_MESSAGE("008", "未知异常，请检查：{}", "未被显示捕获处理的异常数据"),

    REQUEST_METHOD_NOT_SUPPORT("009", "请求方式[{}]不被支持，当前URL仅支持{}类型的请求方式，请检查请求数据是否正确", "请求方式不被支持"),

    CODING_IS_NOT_STANDARDIZED("010", "请检查代码规范:{}", "代码不规范，在开发阶段可避免该异常的产生."),

    MISSING_PARAMETER_OTHER("011", "缺少操作参数，请重试", "未指定缺少参数统一使用."),

    NO_PERMISSION("012", "用户无操作权限", "普通用户调用管理员以上权限接口统一返回."),
    ;

    /**
     * 异常码
     */
    private String code;
    /**
     * 异常信息
     */
    private String message;

    /**
     * 异常描述，无实际作用，仅用于提高异常的可读性
     */
    private String description;

    GlobalECode(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
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
