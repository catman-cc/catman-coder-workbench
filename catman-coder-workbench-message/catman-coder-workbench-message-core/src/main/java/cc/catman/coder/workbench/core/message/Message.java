package cc.catman.coder.workbench.core.message;

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

/**
 * 为什么消息有channel的同时还包含了sender和receiver?
 * 1. 使用channel是为了隔绝消息组,圈定一个业务范围,针对的目标是分组
 * 2. 使用sender和receiver是为了隔离消息的发送者和接收者,针对的目标是个体,达到在channel内依然实现点对点的通信
 * 假设一个相对复杂的业务逻辑,需要多个消息协同完成,那么这些消息的发送者和接收者可能是不同的,但是他们都属于同一个channel
 * 比如: debug模式,一个debug需要独立出一个channel,但是该channel的内部操作,可能需要进行点对点的通信,这时候就需要使用sender和receiver
 *      - 修改指定变量的值,通过一个sendAndWatch这种机制,可以避免非点对点通信模式下,上下文处理的复杂性
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
    private MessageType type=MessageType.UNICAST; // 消息类型
    private String channelId; // 消息的信道唯一标志
    private String channelKind; // 消息的信道类型,该类型用于初始化信道,不同的类型对应不同的信道实例
    private String sender; // 消息发送者,在回复时,该数据将被传递给新消息的receiver字段
    private String receiver; // 消息的接收方,指定消息的接收方,如果为空,则表示消息的接收方是信道的所有者
    private int count; // 消息被处理的次数
    private T payload; // 消息的内容
    private long timestamp; // 消息的时间戳
    private long consumeTime; // 消息的消费时间

    private final Map<String,Object> sendBack=new HashMap<>(); // 消息的回传数据

    @JsonIgnore
    private transient MessageChannel messageChannel;

    public static Message<String> create(String payload){
        return Message.<String>builder()
                .id(UUID.randomUUID().toString())
                .payload(payload).build();
    }

    public static <V> Message<V> create(String topic,V payload){
        return Message.<V>builder()
                .id(UUID.randomUUID().toString())
                .topic(topic)
                .payload(payload).build();
    }

    public static Message<Object> of(Object payload){
        return Message.<Object>builder()
                .id(UUID.randomUUID().toString())
                .timestamp(System.currentTimeMillis())
                .payload(payload).build();
    }

    public static <T> Message<T> of(T payload, MessageChannel messageChannel){
        return Message.<T>builder()
                .id(UUID.randomUUID().toString())
                .timestamp(System.currentTimeMillis())
                .messageChannel(messageChannel)
                .payload(payload).build();
    }

    /**
     * 回复消息,向原有的发送者发送消息,如果存在的话
     * @param message 回复的消息内容
     */
    public void answer(Message<?> message){

        if (StringUtils.hasText(this.sender)){
            if (!StringUtils.hasText(message.receiver)){
                message.setReceiver(this.sender);
            }
        }
        if (StringUtils.hasText(this.channelId)){
            if (!StringUtils.hasText(message.channelId)){
                message.setChannelId(this.channelId);
            }
        }
        if (StringUtils.hasText(this.channelKind)){
            if (!StringUtils.hasText(message.channelKind)){
                message.setChannelKind(this.channelKind);
            }
        }
        if (StringUtils.hasText(this.topic)){
            if (!StringUtils.hasText(message.topic)){
                message.setTopic(this.topic);
            }
        }
        this.messageChannel.send(message);
    }

    public void answer(Message<?> message,String senderId){

        if (StringUtils.hasText(this.sender)){
            if (!StringUtils.hasText(message.receiver)){
                message.setReceiver(this.sender);
            }
        }
        if (StringUtils.hasText(this.channelId)){
            if (!StringUtils.hasText(message.channelId)){
                message.setChannelId(this.channelId);
            }
        }
        if (StringUtils.hasText(this.channelKind)){
            if (!StringUtils.hasText(message.channelKind)){
                message.setChannelKind(this.channelKind);
            }
        }
        if (StringUtils.hasText(this.topic)){
            if (!StringUtils.hasText(message.topic)){
                message.setTopic(this.topic);
            }
        }
        this.messageChannel.send(message);
    }

    /**
     * 判断是否有回传数据
     * @param key 回传数据的key
     * @return 是否有回传数据
     */
    public boolean hasSendBack(String key){
        return sendBack.containsKey(key);
    }

    public boolean hasAllSendBack(String... keys){
        for (String key : keys) {
            if (!sendBack.containsKey(key)){
                return false;
            }
        }
        return true;
    }

    public boolean hasAnySendBack(String... keys){
        for (String key : keys) {
            if (sendBack.containsKey(key)){
                return true;
            }
        }
        return false;
    }

    /**
     * 读取回传数据,如果没有则返回null
     * 该方法只会根据泛型进行强制转换,如果类型不匹配,则会抛出异常
     * @param key 回传数据的key
     * @return 回传数据
     * @param <V> 回传数据的类型
     */
    @SuppressWarnings("unchecked")
    public <V> V getSendBack(String key){
        return (V) sendBack.get(key);
    }

    public int increment(){
        this.setCount(++this.count);
        return this.count;
    }
}
