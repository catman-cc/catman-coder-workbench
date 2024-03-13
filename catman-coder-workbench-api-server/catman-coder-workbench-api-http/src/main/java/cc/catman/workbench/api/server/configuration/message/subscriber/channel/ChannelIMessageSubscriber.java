package cc.catman.workbench.api.server.configuration.message.subscriber.channel;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.subscriber.PostExchangeInjectMessageSubscriber;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import cc.catman.coder.workbench.core.message.system.CreateChannelResponse;
import org.springframework.stereotype.Component;

@Component
public class ChannelIMessageSubscriber implements PostExchangeInjectMessageSubscriber {

    private  IMessageExchange messageExchange;

    @Override
    public boolean isMatch(Message<?> message) {
        return Constants.Message.Topic.Channel.CREATE.equals(message.getTopic());
    }

    @Override
    public MessageResult onReceive(Message<?> message) {
        Message<CreateChannelOptions> msg = message.covert(CreateChannelOptions.class);

        ChannelManager channelManager = messageExchange.getChannelManager();
        // 此时当前消息已经有了消息通道数据,但此时还需要新注册一个消息通道
        MessageChannel channel = channelManager.create(msg.getPayload(), message.getMessageChannel().getConnection());
        Message<Object> resMsg = Message.create(Constants.Message.Topic.Channel.CREATE,CreateChannelResponse.builder().channelId(channel.getId()).success(true).build());
        message.answer(resMsg);
        return MessageResult.ack();
    }

    @Override
    public void injectMessageExchange(IMessageExchange exchange) {
        this.messageExchange=exchange;
    }
}
