package cc.catman.coder.workbench.core.message.channel;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.value.providers.http.HttpEventKind;
import cc.catman.coder.workbench.core.value.report.ReportMessage;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.Optional;


@SuperBuilder
@AllArgsConstructor
public class HttpValueProviderExecutorMessageChannel extends DefaultMessageChannel {
    @Override
    public MessageACK send(Message<?> message) {
        if (message.getPayload() instanceof ReportMessage<?> reportMessage){
            if (Arrays.stream(HttpEventKind.values()).anyMatch(e -> e.name().equals(reportMessage.getEventKind()))){
                // 拦截特定的消息,并且执行对应的处理器
                if (Optional.ofNullable(message.getChannelId()).isEmpty()){
                    message.setChannelId(this.id);
                }
                if (Optional.ofNullable(message.getChannelKind()).isEmpty()){
                    message.setChannelKind("RunSimpleHttpValueProvider");
                }
                this.getConnection().send(message);
                return MessageACK.PENDING;
            }
        }
        return MessageACK.DROP;
    }
}
