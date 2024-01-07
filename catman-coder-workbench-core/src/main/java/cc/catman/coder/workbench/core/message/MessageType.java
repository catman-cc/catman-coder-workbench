package cc.catman.coder.workbench.core.message;

public enum MessageType {
    BROADCAST, // 广播消息,该消息会被所有匹配的消息处理器处理
    UNICAST, // 单播消息,该消息只会被第一个匹配的消息处理器处理
    MULTICAST // 组播消息,该消息会被发送到所有的消息路由器,但是一个消息路由器中只会有一个消息处理器处理
    ;
    public boolean isBroadcast() {
        return this == BROADCAST;
    }

    public boolean isUnicast() {
        return this == UNICAST;
    }

    public boolean isMulticast() {
        return this == MULTICAST;
    }
}
