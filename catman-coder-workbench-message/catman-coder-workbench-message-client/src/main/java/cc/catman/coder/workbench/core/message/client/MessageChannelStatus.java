package cc.catman.coder.workbench.core.message.client;

public enum MessageChannelStatus {
    WAIT,
    READY,
    RETRY,
    STOP
    ;
    public boolean canUse() {
        return this == READY;
    }
}
