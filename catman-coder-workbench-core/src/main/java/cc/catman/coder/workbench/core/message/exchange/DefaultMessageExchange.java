package cc.catman.coder.workbench.core.message.exchange;

import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.MessageExchange;
import cc.catman.coder.workbench.core.message.MessageSubscriber;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
public class DefaultMessageExchange implements MessageExchange {
    @Builder.Default
    private List<MessageSubscriber> subscribers=new ArrayList<>();
    private ObjectMapper objectMapper;
    private ModelMapper modelMapper;
    @Override
    public MessageACK route(JsonTreeMessage message) {
        if (message == null) {
            return null;
        }
        if (message.getType().isBroadcast()){
            return  subscribers.stream().filter(subscriber -> subscriber.getMessageMatch().match(message))
                    .map(subscriber -> {
                        return subscriber.handle(subscriber.createFormatMessage(message));
                    })
                    .filter(Objects::nonNull)
                    .sorted()
                            .findFirst().orElse(MessageACK.NACK);
        }
        // 默认交换器不区分组播和单播
        return subscribers.stream().filter(subscriber -> subscriber.getMessageMatch().match(message))
                    .findFirst()
                    .map(subscriber -> subscriber.handle(message))
                    .orElse(MessageACK.NACK);
    }

    @Override
    public void subscribe(MessageSubscriber subscriber) {
        this.subscribers.add(subscriber);
    }
}
