package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.MatchMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.OnceMessageSubscriber;
import cc.catman.coder.workbench.core.message.subscriber.filter.P2PMessageSubscriberFilter;
import cc.catman.coder.workbench.core.message.subscriber.filter.QAChainMessageSubscriberFilter;
import cc.catman.plugin.core.label.Labels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * 为什么消息有channel的同时还包含了sender和receiver?
 * 1. 使用channel是为了隔绝消息组,圈定一个业务范围,针对的目标是分组
 * 2. 使用sender和receiver是为了隔离消息的发送者和接收者,针对的目标是个体,达到在channel内依然实现点对点的通信
 * 假设一个相对复杂的业务逻辑,需要多个消息协同完成,那么这些消息的发送者和接收者可能是不同的,但是他们都属于同一个channel
 * 比如: debug模式,一个debug需要独立出一个channel,但是该channel的内部操作,可能需要进行点对点的通信,这时候就需要使用sender和receiver
 * - 修改指定变量的值,通过一个sendAndWatch这种机制,可以避免非点对点通信模式下,上下文处理的复杂性
 *
 * @param <T>
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> implements Serializable {
    private String id; // 消息的唯一标志,用于消息的去重
    private String topic; // 消息的主题
    private Labels labels; // 消息标签
    @Builder.Default
    private MessageType type = MessageType.UNICAST; // 消息类型
    private String channelId; // 消息的信道唯一标志
    private String channelKind; // 消息的信道类型,该类型用于初始化信道,不同的类型对应不同的信道实例

    // sender和receiver的作用是为了在channel内实现点对点通信,如果不指定receiver,则表示消息的接收方是信道的所有者
    private String sender; // 消息发送者,在回复时,该数据将被传递给新消息的receiver字段
    private String receiver; // 消息的接收方,指定消息的接收方,如果为空,则表示消息的接收方是信道的所有者

    private int count; // 消息被处理的次数
    private T payload; // 消息的内容
    private long timestamp; // 消息的时间戳
    private long consumeTime; // 消息的消费时间

    private Map<String, Object> sendBack = new HashMap<>(); // 消息的回传数据,消息的接收方在响应时,该数据被包含在back字段中

    private Map<String, Object> back = new HashMap<>(); // 接收到消息,在应答时,会将sendBack中的数据回传给发送者

    /**
     * 消息的带外数据,用于存储消息的附加数据,该数据会被序列化处理,或许改名叫headers?
     */
    private Map<String, Object> headers = new HashMap<>();

    @JsonIgnore
    private transient MessageChannel messageChannel;
    @JsonIgnore
    private transient IMessageDecoderFactory messageDecoderFactory;
    @JsonIgnore
    private transient IMessageExchange messageExchange;
    /**
     * 消息的附加属性,这些属性只会在本地流转,不会被序列化
     */
    @JsonIgnore
    @Builder.Default
    private transient Map<String, Object> attributes = new HashMap<>();

    public Message<T> setHeader(String key, Object value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return this;
    }

    public <T> T getHeader(String key) {
        if (headers == null) {
            return null;
        }
        if (headers.containsKey(key)) {
            return (T) headers.get(key);
        }
        return null;
    }

    public Message<T> setAttr(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
        return this;
    }

    public <T> T getAttr(String key) {
        if (attributes == null) {
            return null;
        }
        if (attributes.containsKey(key)) {
            return (T) attributes.get(key);
        }
        return null;
    }

    public <T> Message<T> covert(Class<T> clazz) {
        if (this.payload == null) {
            return this.reType(clazz);
        }
        if (clazz.isAssignableFrom(this.payload.getClass())) {
            return (Message<T>) this;
        }
        return messageDecoderFactory.decode(this, clazz);
    }

    public <T> Message<T> copy(T payload) {
        return Message.<T>builder()
                .id(UUID.randomUUID().toString())
                .topic(this.topic)
                .labels(this.labels)
                .type(this.type)
                .channelId(this.channelId)
                .channelKind(this.channelKind)
                .sender(this.sender)
                .receiver(this.receiver)
                .count(this.count)
                .payload(payload)
                .timestamp(this.timestamp)
                .consumeTime(this.consumeTime)
                .sendBack(this.sendBack)
                .back(this.back)
                .messageChannel(this.messageChannel)
                .messageDecoderFactory(this.messageDecoderFactory)
                .build();
    }

    public <T> Message<T> reType(Class<T> clazz) {
        if (this.payload != null) {
            throw new RuntimeException("Message payload is not null,can not reType");
        }
        return Message.<T>builder()
                .id(UUID.randomUUID().toString())
                .topic(this.topic)
                .labels(this.labels)
                .type(this.type)
                .channelId(this.channelId)
                .channelKind(this.channelKind)
                .sender(this.sender)
                .receiver(this.receiver)
                .count(this.count)
                .timestamp(this.timestamp)
                .consumeTime(this.consumeTime)
                .sendBack(this.sendBack)
                .back(this.back)
                .messageChannel(this.messageChannel)
                .messageDecoderFactory(this.messageDecoderFactory)
                .build();
    }


    public static Message<String> create(String payload) {
        return Message.<String>builder()
                .id(UUID.randomUUID().toString())
                .payload(payload).build();
    }

    public static <V> Message<V> create(String topic, V payload) {
        return Message.<V>builder()
                .id(UUID.randomUUID().toString())
                .topic(topic)
                .payload(payload).build();
    }

    public static Message<Object> of(Object payload) {
        return Message.<Object>builder()
                .id(UUID.randomUUID().toString())
                .timestamp(System.currentTimeMillis())
                .payload(payload).build();
    }

    public static <T> Message<T> of(T payload, MessageChannel messageChannel) {
        return Message.<T>builder()
                .id(UUID.randomUUID().toString())
                .timestamp(System.currentTimeMillis())
                .messageChannel(messageChannel)
                .payload(payload).build();
    }

    /**
     * 回复消息,向原有的发送者发送消息,如果存在的话
     *
     * @param message 回复的消息内容
     */
    public void answer(Message<?> message) {
        if (StringUtils.hasText(this.sender)) {
            if (!StringUtils.hasText(message.receiver)) {
                message.setReceiver(this.sender);
            }
        }
        if (StringUtils.hasText(this.channelId)) {
            if (!StringUtils.hasText(message.channelId)) {
                message.setChannelId(this.channelId);
            }
        }
        if (StringUtils.hasText(this.channelKind)) {
            if (!StringUtils.hasText(message.channelKind)) {
                message.setChannelKind(this.channelKind);
            }
        }
        if (StringUtils.hasText(this.topic)) {
            if (!StringUtils.hasText(message.topic)) {
                message.setTopic("system:qa");
                message.setHeader("from",this.getId());
            }
        }
        // 回传数据
        message.setBack(this.sendBack);
        this.messageChannel.send(message);
    }

    /**
     * 回复消息,并监听回传数据,但只接受一次回传数据,接受后当前订阅者会被销毁
     * @param message 回复的消息内容
     * @param onReceive 回传数据的处理函数
     */
    public void once(Message<?> message, Function<Message<?>, MessageResult> onReceive) {
        String qaChainId = this.getAttr(QAChainMessageSubscriberFilter.QA_CHAIN_ID);
        qaChainId = StringUtils.hasText(qaChainId) ? qaChainId : UUID.randomUUID().toString();
        message.setAttr(QAChainMessageSubscriberFilter.QA_CHAIN_ID, qaChainId);
        MatchMessageSubscriber ms = MatchMessageSubscriber.builder()
                .attributes(Map.of(QAChainMessageSubscriberFilter.QA_CHAIN_ID,qaChainId))
                .messageMatch((m) -> true)
                .onReceive(onReceive)
                .build();
        OnceMessageSubscriber once = OnceMessageSubscriber.of(ms);
        getMessageExchange().add(once);
        this.answer(message);
    }

    public IMessageSubscriber answer(Message<?> message, Function<Message<?>, MessageResult> onReceive) {
        String qaChainId = this.getAttr(QAChainMessageSubscriberFilter.QA_CHAIN_ID);
        qaChainId = StringUtils.hasText(qaChainId) ? qaChainId : UUID.randomUUID().toString();
        message.setAttr(QAChainMessageSubscriberFilter.QA_CHAIN_ID, qaChainId);
        MatchMessageSubscriber ms = MatchMessageSubscriber.builder()
                .attributes(Map.of(QAChainMessageSubscriberFilter.QA_CHAIN_ID,qaChainId))
                .messageMatch((m) -> true)
                .onReceive(onReceive)
                .build();
        getMessageExchange().add(ms);
        this.answer(message);
        return ms;
    }

    public void answer(Message<?> message, String senderId) {
        if (StringUtils.hasText(this.sender)) {
            if (!StringUtils.hasText(message.receiver)) {
                message.setReceiver(this.sender);
            }
        }
        if (StringUtils.hasText(this.channelId)) {
            if (!StringUtils.hasText(message.channelId)) {
                message.setChannelId(this.channelId);
            }
        }
        if (StringUtils.hasText(this.channelKind)) {
            if (!StringUtils.hasText(message.channelKind)) {
                message.setChannelKind(this.channelKind);
            }
        }
        if (StringUtils.hasText(this.topic)) {
            if (!StringUtils.hasText(message.topic)) {
                message.setTopic(this.topic);
            }
        }
        this.messageChannel.send(message);
    }

    /**
     * 创建一个P2P的信道,即点对点信道,该信道其实是一个特殊的订阅者
     *
     * @param sender   发送者id
     * @param receiver 接收者id
     */
    public IMessageSubscriber createP2PChannel(String sender, String receiver, Function<Message<?>, MessageResult> onReceive) {
        MatchMessageSubscriber ms = MatchMessageSubscriber.builder()
                .attributes(Map.of("p2p", true
                        , P2PMessageSubscriberFilter.PROPERTY_RECEIVER_NAME, receiver
                        , P2PMessageSubscriberFilter.PROPERTY_SENDER_NAME, sender)
                )
                .messageMatch((m) -> true)
                .onReceive(onReceive)
                .build();
        getMessageExchange().add(ms);
        return ms;
    }

    public IMessageSubscriber createP2PChannel(Function<Message<?>, MessageResult> onReceive) {
        return createP2PChannel(UUID.randomUUID().toString(), UUID.randomUUID().toString(), onReceive);
    }

    /**
     * 判断是否有回传数据
     *
     * @param key 回传数据的key
     * @return 是否有回传数据
     */
    public boolean hasSendBack(String key) {
        return sendBack.containsKey(key);
    }

    public boolean hasAllSendBack(String... keys) {
        for (String key : keys) {
            if (!sendBack.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnySendBack(String... keys) {
        for (String key : keys) {
            if (sendBack.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 读取回传数据,如果没有则返回null
     * 该方法只会根据泛型进行强制转换,如果类型不匹配,则会抛出异常
     *
     * @param key 回传数据的key
     * @param <V> 回传数据的类型
     * @return 回传数据
     */
    @SuppressWarnings("unchecked")
    public <V> V getSendBack(String key) {
        return (V) sendBack.get(key);
    }

    public int increment() {
        this.setCount(++this.count);
        return this.count;
    }
}
