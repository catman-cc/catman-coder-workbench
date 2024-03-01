package cc.catman.workbench.api.server.configuration.message.subscriber;

import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.function.FunctionInfo;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.workbench.service.core.services.IFunctionCallService;
import cc.catman.workbench.service.core.services.IFunctionInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FunctionCallInfoMessageSubscriber extends AbstractAntPathMessageSubscriber<FunctionInfo>{
    @Resource
    private IFunctionCallService functionCallService;

    @Resource
    private IFunctionInfoService functionInfoService;

    @Override
    protected String getPrefix() {
        return "/function-call/create";
    }

    protected MessageResult doMessage(Message<FunctionInfo> message, Map<String, String> variables) {
        // 理论上此处应该是一个FunctionInfo实例,但实际上有可能只有kind值,比如if,for等
        // 因此,这里还需要对FunctionInfo进一步处理,才能够传递给service执行创建
        // 比如,if这种内置函数,其本身是不会持久化的,需要从另一个数据源获取对应的数据
        // 所以,需要一个函数注册机,该注册机负责填充函数数据,以及提供给调用者所有函数信息?
        FunctionInfo payload =message.getPayload();
        if (payload == null){
            return MessageResult.builder().ack(MessageACK.DROP).doNext(false).build();
        }
        // 加载函数信息
        payload=functionInfoService.fillIfNeed(payload);
        // 创建函数
        FunctionCallInfo functionCallInfo = functionCallService.create(payload);
        // 执行应答操作
        message.answer(Message.of(functionCallInfo));
        return MessageResult.builder().ack(MessageACK.ACK).doNext(false).build();
    }
}
