package cc.catman.coder.workbench.core.message;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息结果
 */
@Data
@Builder
public class MessageResult {

    /**
     * 消息处理应答码
     */
    private MessageACK ack;

    /**
     * 是否交由其他处理器继续处理
     */
    private boolean doNext;

    /**
     * 被订阅者处理的次数
     */
    private int handlerCount;

    /**
     * 记录处理过程中的详细信息
     */
    @Builder.Default
    private List<Object> handlerDetails=new ArrayList<>();

    public static MessageResult ack(){
        return MessageResult.builder().ack(MessageACK.ACK).doNext(false).build();
    }
    public static MessageResult drop(){
        return MessageResult.builder().ack(MessageACK.DROP).doNext(false).build();
    }
    public static MessageResult of(MessageACK ack){
        return MessageResult.builder().ack(ack).doNext(false).build();
    }

    public static MessageResult of(MessageACK ack, boolean doNext) {
        return MessageResult.builder().ack(ack).doNext(doNext).build();
    }

    public MessageResult finish() {
      return finish(MessageACK.ACK);
    }

    public MessageResult finish(MessageACK ack) {
        this.ack = ack;
        this.doNext = false;
        return this;
    }
}
