package cc.catman.workbench.api.server.configuration.message;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.match.AntPathMessageMatch;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;
import cc.catman.coder.workbench.core.type.SimpleTypeAnalyzer;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MessageSubscriberConfiguration implements InitializingBean {

    @Resource
    private MessageExchange exchange;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private ModelMapper modelMapper;
    @Override
    public void afterPropertiesSet() throws Exception {
        new MessageSubscriber<SimpleTypeAnalyzer.TypeDesc>(){
                  @Override
            public MessageMatch getMessageMatch() {
                return AntPathMessageMatch.of("/tools/simple-type-analyzer/**");
            }

            @Override
            public MessageACK handle(Message<SimpleTypeAnalyzer.TypeDesc> message) {
                SimpleTypeAnalyzer.TypeDesc td = message.getPayload();
                TypeDefinition analyzer = SimpleTypeAnalyzer.of(td).analyzer();
                message.answer(Message.of(analyzer));
                return MessageACK.ACK;
            }

            @Override
            @SneakyThrows
            public Message<SimpleTypeAnalyzer.TypeDesc> createFormatMessage(JsonTreeMessage message) {
                Message<SimpleTypeAnalyzer.TypeDesc> formatMessage = new Message<>();
                modelMapper.map(message,formatMessage);
                formatMessage.setPayload(objectMapper.treeToValue(message.getPayload(),SimpleTypeAnalyzer.TypeDesc.class));
                return formatMessage;
            }
        }.subscribe(exchange);

    }
}
