package cc.catman.coder.workbench.core.message.ack;

import cc.catman.coder.workbench.core.message.validator.IMessageValidator;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.validator.ValidateResult;

import java.util.Optional;

/**
 * 消息确认消息验证器,当消息确认机制启用时,要求消息必须拥有唯一标志,
 * 后续消息会注入到消息的发送过程中,同步发送将强制阻塞到收到确认消息,异步发送则可以通过回调机制处理
 *
 * ACK验证机制,还没有想好,常规的消息确认机制基本是消息送达即确认.
 * 但我还没想好是否需要考虑提供消息处理完成的确认机制,这个需要考虑一下
 * 获取提供一个事物机制
 */
public class AckMessageValidator implements IMessageValidator {
    @Override
    public ValidateResult validate(Message<?> message) {
        return Optional.ofNullable(message.getId()).map(id -> ValidateResult.success()).orElse(ValidateResult.fail("消息ID不能为空"));
    }
}
