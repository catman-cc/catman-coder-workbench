package cc.catman.coder.workbench.core.message.client;

import cc.catman.coder.workbench.core.message.Message;

public interface ICommunicationClient {
    void send(Message<?> message);
}
