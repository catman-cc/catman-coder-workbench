## 消息客户端

目前主要提供了一个使用netty实现的Websocket客户端，可以用于连接到Websocket服务器，发送和接收消息。

核心类是`DefaultMessageClient`，它实现了`MessageClient`接口，提供了连接、发送和接收消息的功能,同时屏蔽了底层协议.

需要两个构造参数:
```java
DefaultMessageClient(MessageConnection<?> connection, IMessageExchange exchange);
```

## 使用示例

```java
public static void main(String[] args) {
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
    // ...
}
```
mc有两种创建信道的方法,一种是通过回调方法创建异步的信道:
```java
public static void main(String[] args) {
    // ...
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
}
```
另一种是通过同步方法创建信道:
```java
public static void main(String[] args) {
    MessageChannel channel = mc.createSync(CreateChannelOptions.builder()
            .channelId(UUID.randomUUID().toString())
            .kind("default")
            .build());
    channel.onMessage(message -> {
        System.out.println("sync message: " + message);
    });

    Message<Map<String, String>> msg = Message.create(Constants.Message.Topic.System.PING, Map.of("hello", "world"));
    channel.send(msg);
}
```