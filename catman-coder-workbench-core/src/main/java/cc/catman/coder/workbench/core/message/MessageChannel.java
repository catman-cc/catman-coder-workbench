package cc.catman.coder.workbench.core.message;

/**
 * 消息信道,一个链接可以有多个信道,每个信道可以有多个消息处理器,channel是一个特殊的链接
 * 为了信道的复用,应该提供一个绑定器来记录
 */
public interface MessageChannel {
    String getId();
  /**
   * 获取信道对应的链接,一个信道只能对应一个链接,但是需要注意的是,信道
   */
  MessageConnection<?> getConnection();

  void onMessage(Message<?> message,MessageContext context);

  MessageACK send(Message<?> message);

  void send(Message<?> message, MessageHandlerCallback<?> callback);
}
