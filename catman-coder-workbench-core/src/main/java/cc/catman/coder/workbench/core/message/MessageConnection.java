package cc.catman.coder.workbench.core.message;


/**
 * 消息连接
 */
public interface MessageConnection<T> {
    /**
     * 获取连接ID
     */
    String getId();

    /**
     * 获取连接类型
     */
    String getType();

    T getRawConnection();

    boolean isAlive();

    MessageContext getContext();

    MessageACK send(Message<?> message);

}
