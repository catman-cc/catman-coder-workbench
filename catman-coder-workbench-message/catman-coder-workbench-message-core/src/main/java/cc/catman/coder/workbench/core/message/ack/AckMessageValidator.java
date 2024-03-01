package cc.catman.coder.workbench.core.message.ack;

import cc.catman.coder.workbench.core.message.validator.IMessageValidator;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.validator.ValidateResult;

import java.util.Optional;

/**
 * 消息确认消息验证器,当消息确认机制启用时,要求消息必须拥有唯一标志
 */
public class AckMessageValidator implements IMessageValidator {
    @Override
    public ValidateResult validate(Message<?> message) {
        return Optional.ofNullable(message.getId()).map(id -> ValidateResult.success()).orElse(ValidateResult.fail("消息ID不能为空"));
    }
}
