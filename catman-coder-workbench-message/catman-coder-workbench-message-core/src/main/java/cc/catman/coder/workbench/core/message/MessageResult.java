package cc.catman.coder.workbench.core.message;

/**
 * 消息结果
 */
public class MessageResult {

    /**
     * 消息处理应答码
     */
    private MessageACK ack;

    /**
     * 是否交由其他处理器继续处理
     */
    private boolean doNext;

    public MessageResult finish() {
      return finish(MessageACK.ACK);
    }

    public MessageResult finish(MessageACK ack) {
        this.ack = ack;
        this.doNext = false;
        return this;
    }
}
