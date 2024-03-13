package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Constants;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageResult;

import java.util.Map;
import java.util.Optional;
/**
 * 消息处理环绕,用于处理消息处理前后的逻辑
 */
public class ACKMessageSurround implements IMessageSurround{
    @Override
    public void after(Message<?> message, MessageResult result) {
        // 回传一条消息,表示消息已经处理完成
        Message<Map<String, String>> msg = Message.create("ack", Map.of(
                "msgId", Optional.ofNullable(message.getId()).orElse(""),
                "res", "ack"
        ));
        fillChannelInfo(msg);
        message.answer(msg);
    }

    @Override
    public void onError(Message<?> message, Throwable e, MessageResult result) {
        // 回传一条消息,表示消息已经处理完成
        Message<Map<String, String>> msg = Message.create("ack", Map.of(
                "msgId", Optional.ofNullable(message.getId()).orElse(""),
                "res", "err",
                "reason", e.getMessage()
        ));
        fillChannelInfo(msg);
        message.answer(msg);
    }

    private static void fillChannelInfo(Message<Map<String, String>> msg) {
        msg.setChannelId(Constants.Message.Channel.SYSTEM_ID);
        msg.setChannelKind(Constants.Message.Channel.SYSTEM_KIND);
    }
}
