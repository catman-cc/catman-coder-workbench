package cc.catman.coder.workbench.core.message.client.nws;

import cc.catman.coder.workbench.core.message.MessageConnection;

public interface WebSocketTextMessageHandler {
    void onMessage(String text, MessageConnection<?> connection);
}
