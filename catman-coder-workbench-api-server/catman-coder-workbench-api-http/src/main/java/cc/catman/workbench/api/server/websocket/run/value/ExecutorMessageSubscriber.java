package cc.catman.workbench.api.server.websocket.run.value;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.match.AntPathMessageMatch;
import cc.catman.coder.workbench.core.message.message.JsonTreeMessage;

import cc.catman.coder.workbench.core.message.subscriber.Command;
import cc.catman.coder.workbench.core.value.ValueProviderExecutor;
import cc.catman.coder.workbench.core.value.ValueProviderRegistry;
import cc.catman.coder.workbench.core.value.context.DefaultValueProviderContextManager;
import cc.catman.coder.workbench.core.value.providers.http.HttpValueProviderResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.support.GenericConversionService;


@Slf4j
@Builder
public class ExecutorMessageSubscriber implements MessageSubscriber<Command> {

    private ValueProviderExecutor executor;

    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    private ValueProviderRegistry valueProviderRegistry;
    private GenericConversionService convertService;

    private GroupedMessageQueue<Message<?>> messageQueue;
    @Override
    @SneakyThrows
    public MessageACK handle(Message<Command> message) {
        // 根据命令的类型决定执行具体的操作
        // 从上下文中获取批处理id
        DebuggableValueProviderExecutor ex = DebuggableValueProviderExecutor.builder()
                .genericConversionService(convertService)
                .valueProviderRegistry(valueProviderRegistry)
                .contextManager(new DefaultValueProviderContextManager())
                .messageChannel(message.getMessageChannel())
                .parameterParseStrategies(executor.getParameterParseStrategies())
                .messageQueue(messageQueue)
                .build();

        Command payload = message.getPayload();
        // channelId对应着batchId
        // 如果是run命令,则需要创建一个新的执行上下文
//        ValueProviderContext c = executor.createValueProviderContext(payload.getValueProviderDefinition(), payload.getVariables());
        long start=System.currentTimeMillis();
        try {
            HttpValueProviderResult exec = ex.exec(payload.getValueProviderDefinition(), payload.getVariables(),HttpValueProviderResult.class);
            // 往上下文中注入断点等数据
            // 注入断点
            // 获取channel对应的执行上下文,如果上下文不存在
            Message<Object> msg = Message.of(exec);
            msg.setConsumeTime(System.currentTimeMillis()-start);
        }catch (Exception e) {
            log.error("执行失败", e);
            Message<Object> msg = Message.of(e);
            msg.setConsumeTime(System.currentTimeMillis() - start);
            message.answer(msg);
        }
        // 理论上answer方法也要放入到队列中进行处理,否则会发生错误:The remote endpoint was in state [TEXT_PARTIAL_WRITING] which is an invalid state for called method
//        message.answer(msg);
        return null;
    }

    @Override
    @SneakyThrows
    public Message<Command> createFormatMessage(JsonTreeMessage message) {
        Message<Command> cm=new Message<>();
        modelMapper.map(message,cm);
        cm.setPayload(objectMapper.treeToValue(message.getPayload(),Command.class));
        return cm;
    }


    @Override
    public boolean subscribe(MessageExchange exchange) {
         exchange.subscribe(this);
         return true;
    }

    @Override
    public MessageMatch getMessageMatch() {
        return AntPathMessageMatch.of("executor/**");
    }
}
