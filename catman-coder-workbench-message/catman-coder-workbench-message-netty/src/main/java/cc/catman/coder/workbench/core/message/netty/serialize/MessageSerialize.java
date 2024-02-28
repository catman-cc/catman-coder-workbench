package cc.catman.coder.workbench.core.message.netty.serialize;

import cc.catman.coder.workbench.core.message.Message;

public interface MessageSerialize {
    Message<?> decode(byte[] bytes);

    byte[] encode(Message<?> message);
}
