package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.MessageExchange;
import cc.catman.coder.workbench.core.message.MessageSubscriber;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
public class ChannelMessageExchange implements MessageExchange {
    @Builder.Default
    private List<MessageSubscriber> subscribers=new ArrayList<>();
    @Override
    public MessageACK route(JsonTreeMessage message) {
        return null;
    }

    @Override
    public void subscribe(MessageSubscriber subscriber) {
    }
}
