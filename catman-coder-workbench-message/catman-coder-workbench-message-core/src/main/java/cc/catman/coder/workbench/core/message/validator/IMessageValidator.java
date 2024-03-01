package cc.catman.coder.workbench.core.message.validator;

import cc.catman.coder.workbench.core.message.Message;

public interface IMessageValidator {
    ValidateResult validate(Message<?> message);
}
