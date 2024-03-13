package cc.catman.coder.workbench.core.message.client;

import cc.catman.coder.workbench.core.message.*;
import cc.catman.coder.workbench.core.message.channel.DefaultMessageChannel;
import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import cc.catman.coder.workbench.core.message.system.CreateChannelResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMessageClient implements IMessageClient {
    private MessageConnection<?> connection;

    private IMessageExchange exchange;

    @Builder.Default
    private Map<String, MessageChannelHolder> channelHolders=new ConcurrentHashMap<>();

    public DefaultMessageClient(MessageConnection<?> connection, IMessageExchange exchange) {
        this.connection = connection;
        this.exchange = exchange;
        this.channelHolders=new ConcurrentHashMap<>();
        this.postConstruct();
    }

    /**
     * 作为客户端时,需要默认注册一个根据channelId进行分发的消息订阅者
     */
    protected void postConstruct() {
        IMessageSubscriberManager subscriberManager = exchange.getSubscriberManager();
        subscriberManager.add(message -> {
            if (!Constants.Message.Topic.Channel.CREATE.equals(message.getTopic())){
                return true;
            }
            Message<CreateChannelResponse> msg = message.covert(CreateChannelResponse.class);
            CreateChannelResponse res = msg.getPayload();
            if (!res.isSuccess()) {
                // TODO: log.error("create channel failed, reason: {}", res.getReason());
            }

            String channelId = res.getChannelId();
            if (!this.channelHolders.containsKey(channelId)) {
                // TODO
            }

            MessageChannelHolder holder = this.channelHolders.get(channelId);
            DefaultMessageChannel dmc = DefaultMessageChannel
                    .builder()
                    .connection(this.connection)
                    .channelManager(this.exchange.getChannelManager())
                    .id(channelId)
                    .build();
            synchronized (holder){
                holder.setChannel(dmc);
                holder.setStatus(MessageChannelStatus.READY);
                holder.notifyAll();
            }
            // 执行回调
            Optional.ofNullable(holder.getCallback()).ifPresent(c->c.callback(dmc));
            return true;
        });

        // 其余消息按照channelId进行分发
        subscriberManager.add((m)->true,message -> {
            MessageChannelHolder holder = this.channelHolders.get(message.getChannelId());
            if (!holder.getStatus().canUse()){
                // 不能使用
                return MessageResult.drop();
            }
            holder.getChannel().onMessage(message);
            return MessageResult.ack();
        });

    }

    @Override
    public void create(CreateChannelOptions options, ICreateChannelCallback callback) {
        Optional.ofNullable(options).orElseThrow(() -> new IllegalArgumentException("options is null"));
        Optional.ofNullable(callback).orElseThrow(() -> new IllegalArgumentException("callback is null"));
        if (Optional.ofNullable(options.getChannelId()).isEmpty()) {
            options.setChannelId(UUID.randomUUID().toString());
        }
        MessageChannelHolder holder = new MessageChannelHolder();
        holder.setOptions(options);
        holder.setStatus(MessageChannelStatus.WAIT);
        holder.setCallback(callback);
        this.channelHolders.put(options.getChannelId(), holder);
        Message<CreateChannelOptions> msg = Message.create(Constants.Message.Topic.Channel.CREATE, options);
        this.getConnection().send(msg);
    }

    public MessageChannel createSync(CreateChannelOptions options){
        Optional.ofNullable(options).orElseThrow(() -> new IllegalArgumentException("options is null"));
        if (Optional.ofNullable(options.getChannelId()).isEmpty()) {
            options.setChannelId(UUID.randomUUID().toString());
        }
        MessageChannelHolder holder = new MessageChannelHolder();
        holder.setOptions(options);
        holder.setStatus(MessageChannelStatus.WAIT);
        this.channelHolders.put(options.getChannelId(), holder);
        Message<CreateChannelOptions> msg = Message.create(Constants.Message.Topic.Channel.CREATE, options);
        this.getConnection().send(msg);
        // 然后等待创建完成
        synchronized (holder){
            if (Optional.ofNullable(holder.getChannel()).isPresent()){
                return holder.getChannel();
            }
            // 如果没有,等待唤醒
            try {
                holder.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return holder.getChannel();
    }
}
