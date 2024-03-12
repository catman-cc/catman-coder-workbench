package cc.catman.workbench.api.server.configuration.message.subscriber.channel;

import cc.catman.coder.workbench.core.message.ChannelManager;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.subscriber.PostExchangeInjectMessageSubscriber;
import cc.catman.coder.workbench.core.message.system.CreateChannel;
import cc.catman.coder.workbench.core.message.system.CreateChannelResponse;
import org.springframework.stereotype.Component;

@Component
public class ChannelIMessageSubscriber implements PostExchangeInjectMessageSubscriber {

    private  IMessageExchange messageExchange;

    @Override
    public boolean isMatch(Message<?> message) {
        return "system/channel/create".equals(message.getTopic());
    }

    @Override
    public MessageResult onReceive(Message<?> message) {
        Message<CreateChannel> msg = message.covert(CreateChannel.class);

        ChannelManager channelManager = messageExchange.getChannelManager();
        // 此时当前消息已经有了消息通道数据,但此时还需要新注册一个消息通道
        MessageChannel channel = channelManager.create(msg.getPayload(), message.getMessageChannel().getConnection());
        message.answer(Message.of(CreateChannelResponse.builder().channelId(channel.getId()).success(true).build()));
        return MessageResult.ack();
    }

    @Override
    public void injectMessageExchange(IMessageExchange exchange) {
        this.messageExchange=exchange;
    }
}
