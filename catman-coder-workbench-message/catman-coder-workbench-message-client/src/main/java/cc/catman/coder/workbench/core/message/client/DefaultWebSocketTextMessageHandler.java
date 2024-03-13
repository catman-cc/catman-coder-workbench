package cc.catman.coder.workbench.core.message.client;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.client.nws.WebSocketTextMessageHandler;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerialize;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeFactory;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class DefaultWebSocketTextMessageHandler implements WebSocketTextMessageHandler {
    private MessageSerializeFactory factory;
    private IMessageExchange messageExchange;

    public DefaultWebSocketTextMessageHandler(MessageSerializeFactory factory, IMessageExchange messageExchange) {
        this.factory = factory;
        this.messageExchange = messageExchange;
    }

    @Override
    public void onMessage(String text,MessageConnection<?> connection) {
        byte[] bytes = this.convertToByteArray(text);
        if (bytes.length<1){
            return;
        }
        Optional<MessageSerialize> serialize = this.factory.getSerialize(bytes[0]);
        if(serialize.isEmpty()){
            throw new RuntimeException("can not find serializer,with type:"+bytes[0]);
        }
        MessageSerialize ms = serialize.get();
        byte[] msgBytes=new byte[bytes.length-1];
        System.arraycopy(bytes,1,msgBytes,0,msgBytes.length);
        Message<?> message = ms.decode(msgBytes);
        messageExchange.exchange(message,connection);
    }

    private byte[] convertToByteArray(String text){
        return StringUtils.hasText(text)?text.getBytes():new byte[0];
    }
}
