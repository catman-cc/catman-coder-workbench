package cc.catman.coder.workbench.core.message.system;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;

import java.util.Optional;

/**
 * 系统核心消息订阅者,用于处理系统核心消息
 */
public class SystemMessageSubscriber implements IMessageSubscriber {
    @Override
    public boolean isMatch(Message<?> message) {
        return Optional.ofNullable(message.getTopic()).map(topic-> topic.startsWith("/system/channel/create")).orElse(false);
    }

    @Override
    public MessageResult onReceive(Message<?> message) {
        // 处理系统消息,并进行分发工作

        // 比如,信道相关的信息,分发给信道管理器
        Message<CreateChannel> msg = message.covert(CreateChannel.class);


        CreateChannel createChannel=null;
        // 由信道管理器创建信道
        MessageChannel channel=null;

        // 得到信道之后,回传给客户端,表示信道创建成功,当前操作完成
        return null;
    }
}
