package cc.catman.workbench.api.server.configuration.exception.ecodes;

import cc.catman.workbench.api.server.configuration.exception.annotations.Prefix;
import jakarta.validation.constraints.*;
import org.checkerframework.checker.units.qual.Length;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring Valid 统一异常码支持
 */
@Prefix("VALID")
public enum ValidECode implements ECode<String> {
    UNKNOWN("999", "数据对象[{}]未通过验证，{}", Unknown.class, "未知异常"),
    NULL("001", "数据对象[{}]必须为null，{}。", Null.class, "验证对象是否为null"),
    NOT_BLANK("002", "字符串对象[{}]不得为空，{}", NotBlank.class, "检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格."),
    NOT_EMPTY("003", "集合对象[{}]不得为空，{}", NotEmpty.class, "检查约束元素是否为NULL或者是EMPTY."),
    NOT_NULL("004", "数据对象[{}]为必填项，{}。", NotNull.class, "验证对象是否不为null, 无法查检长度为0的字符串"),

    ASSERT_FALSE("005", "boolean对象[{}]必需为false,{}", AssertFalse.class, "验证 Boolean 对象是否为 false"),
    ASSERT_TRUE("006", "boolean对象[{}]必需为true,{}", AssertTrue.class, "验证 Boolean 对象是否为 true"),

    SIZE("007", "集合对象[{}]大小有误，{}", Size.class, "验证对象（Array,Collection,Map,String）长度是否在给定的范围之内"),
    LENGTH("008", "字符串对象[{}]长度有误，{}", Length.class, "验证字符串长度是否在给定的范围之内"),

    PAST("009", "时间对象[{}]必须在当前时间之前，{}", Past.class, "验证 Date 和 Calendar 对象是否在当前时间之前"),
    FUTURE("010", "时间对象[{}]必须在当前时间之后，{}", Future.class, "验证 Date 和 Calendar 对象是否在当前时间之后"),

    PATTERN("011", "字符串对象[{}]格式错误，{}", Pattern.class, "验证 String 对象是否符合正则表达式的规则"),

    MAX("012", "数据对象[{}]超出限制，{}", Max.class, "验证 Number 和 String 对象是否小等于指定的值"),
    MIN("013", "数据对象[{}]超出限制，{}", Min.class, "验证 Number 和 String 对象是否大等于指定的值"),
    DECIMAL_MAX("014", "数据对象[{}]超出限制，{}", DecimalMax.class, "被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度"),
    DECIMAL_MIN("015", "数据对象[{}]超出限制，{}", DecimalMin.class, "被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度"),

    DIGITS("016", "参数[{}]不能转换为指定格式的数字,{}", Digits.class, " 验证字符串/数字是否是符合指定格式的数字，interger指定整数精度，fraction指定小数精度。 "),
    EMAIL("017", "邮件地址参数[{}]格式错误，{}", Email.class, "验证是否是邮件地址，如果为null,不进行验证，算通过验证。"),

    INVALID_PARAMETER_FORMAT("018", "数据对象[{}]未通过验证，{}", ParameterFormat.class, "参数格式异常"),

    FUTURE_OR_PRESENT("", "", FutureOrPresent.class, ""),

    NEGATIVE("", "", Negative.class, ""),
    NEGATIVE_OR_ZERO("", "", NegativeOrZero.class, ""),

    PAST_OR_PRESENT("1001", "参数[{}]不得为空，{}", PastOrPresent.class, ""),

    POSITIVE("1001", "参数[{}]不得为空，{}", Positive.class, ""),
    POSITIVE_OR_ZERO("1001", "参数[{}]不得为空，{}", PositiveOrZero.class, ""),


    ;

    private static ConcurrentHashMap<Class<? extends Annotation>, ValidECode> cache = new ConcurrentHashMap<>();

    private String code;
    private String message;
    private Class<? extends Annotation> validAnnotation;
    private String description;

    ValidECode(String code, String message, Class<? extends Annotation> validAnnotation, String description) {
        this.code = code;
        this.message = message;
        this.validAnnotation = validAnnotation;
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


    public static ValidECode with(Class<? extends Annotation> valid) {
        Optional<ValidECode> code = Arrays.stream(values()).filter((v) -> v.validAnnotation.equals(valid)).findAny();
        return code.orElse(UNKNOWN);
    }

    public static ValidECode with(String valid) {
        Optional<ValidECode> code = Arrays.stream(values()).filter((v) -> v.validAnnotation.getSimpleName().equals(valid)).findAny();
        return code.orElse(UNKNOWN);
    }

    private static @interface Unknown {
    }

    private static @interface ParameterFormat {
    }
}
