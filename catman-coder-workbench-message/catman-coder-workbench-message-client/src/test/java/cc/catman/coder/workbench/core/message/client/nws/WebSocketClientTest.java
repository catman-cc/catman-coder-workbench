package cc.catman.coder.workbench.core.message.client.nws;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.client.DefaultMessageClient;
import cc.catman.coder.workbench.core.message.client.WebsocketMessageConnection;
import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;


class WebSocketClientTest {

    @Test
    void start() {
        ObjectMapper objectMapper = new ObjectMapper();
        IMessageExchange messageExchange = new DefaultMessageExchange();
        messageExchange.getMessageDecoderFactory().add(new ObjectMapperMessageDecoder(objectMapper));        // 重写消息的通道信息,ack和default这两个默认通道

        messageExchange.add(message -> {
            try {
                System.out.println(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return true;
        });

        WebSocketClientConfiguration config = WebSocketClientConfiguration.builder()
                .url("ws://127.0.0.1:8080/message")
                .objectMapper(objectMapper)
                .messageExchange(messageExchange)
                .textMessageHandler(new SimpleWebSocketTextMessageHandler(objectMapper, messageExchange))
                .build();

        WebSocketClient webSocketClient = WebSocketClient.of(config).start();
        WebsocketMessageConnection connection = WebsocketMessageConnection.builder().id(UUID.randomUUID().toString())
                .rawConnection(webSocketClient).build();

        DefaultMessageClient mc = new DefaultMessageClient(connection, messageExchange);

        mc.create(CreateChannelOptions.builder()
                .channelId(UUID.randomUUID().toString())
                .kind("default")
                .build(), channel -> {

            Message<Map<String, String>> msg = Message.create(Constants.Message.Topic.System.PING, Map.of("hello", "world"));
            channel.send(msg);
            channel.onMessage(message -> {
                System.out.println("message: " + message);
            });
        });

        MessageChannel channel = mc.createSync(CreateChannelOptions.builder()
                .channelId(UUID.randomUUID().toString())
                .kind("default")
                .build());
        channel.onMessage(message -> {
            System.out.println("sync message: " + message);
        });

        Message<Map<String, String>> msg = Message.create(Constants.Message.Topic.System.PING, Map.of("hello", "world"));
        channel.send(msg);


//        webSocketClient.await();
    }
}