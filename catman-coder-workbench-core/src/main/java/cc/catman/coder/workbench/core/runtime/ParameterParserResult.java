package cc.catman.coder.workbench.core.runtime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParameterParserResult {
    @Getter
    boolean parsed;

    Object result;

    Exception exception;

    public static ParameterParserResult parsed(Object result) {
        return new ParameterParserResult(true, result, null);
    }

    public static ParameterParserResult notParsed() {
        return new ParameterParserResult(false, null, null);
    }

    public static ParameterParserResult error(Exception e) {
        return new ParameterParserResult(false, null, e);
    }

    public ParameterParserResult(boolean parsed, Object result, Exception exception) {
        this.parsed = parsed;
        this.result = result;
        this.exception = exception;
    }

    public boolean hasException() {
        return exception != null;
    }

}
