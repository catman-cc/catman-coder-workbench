package cc.catman.coder.workbench.core.message.validator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateResult {
    private boolean success;
    private String message;

    public static ValidateResult success() {
        return new ValidateResult(true, null);
    }

    public static ValidateResult fail(String message) {
        return new ValidateResult(false, message);
    }

    public ValidateResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
