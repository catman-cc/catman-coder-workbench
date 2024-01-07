package cc.catman.coder.workbench.core.message;

public enum MessageACK {
    ACK, // 消息处理成功
    NACK, // 消息处理失败
    PENDING, // 消息处理中,
    DROP // 消息丢弃
    ;
    public boolean success() {
        return this == ACK;
    }

    public boolean fail() {
        return this == NACK;
    }

    public boolean pending() {
        return this == PENDING;
    }

    public static boolean isSuccess(MessageACK ack) {
        return ack != null && ack.success();
    }

    public static boolean isFail(MessageACK ack) {
        return ack != null && ack.fail();
    }
    public static boolean isPending(MessageACK ack) {
        return ack != null && ack.pending();
    }

}
