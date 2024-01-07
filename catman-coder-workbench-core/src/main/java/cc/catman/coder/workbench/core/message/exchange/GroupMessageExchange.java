package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;

import java.util.List;

public class GroupMessageExchange implements MessageExchange {
    private List<MessageExchange> exchanges;

    @Override
    public MessageACK route(JsonTreeMessage message) {
        if (message.getType().isUnicast()){
            return exchanges.stream().filter(exchange -> exchange instanceof DefaultMessageExchange)
                    .map(exchange -> exchange.route(message))
                    .filter(messageACK -> messageACK.success()|| messageACK.pending())
                    .findFirst().orElse(MessageACK.NACK);
        }

            return exchanges.stream().map(exchange -> exchange.route(message )).sorted()
                    .findFirst().orElse(MessageACK.NACK);
    }

    @Override
    public void subscribe(MessageSubscriber subscriber) {

    }
}
