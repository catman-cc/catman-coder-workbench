package cc.catman.coder.workbench.core.message.exchange.strategy;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriberManager;

/**
 * 点对点消息交换策略
 * 该策略,要求必须先建立P2P信道,然后才能进行消息交换
 * 因此,想要创建P2P信道,必须先由客户端发起P2P请求,然后服务端返回给客户端接收和发送消息的唯一标志
 * 通过这个唯一标志,客户端和服务端就可以建立P2P信道,然后进行消息交换
 *
 * 具体流程是:
 * 1. 客户端发送单播消息给服务端,请求建立P2P信道
 * 2. 服务端接收到消息后,返回给客户端两个唯一标志,一个是接收消息的唯一标志,一个是发送消息的唯一标志
 * 3. 客户端接收到服务端返回的消息后,就可以将MessageType设置为P2P,然后进行消息交换
 *
 * 重点在于2,这要求系统必须提供一个默认的topic范围,用于处理,比如topic是 /system/createP2PChannel,然后携带一些back参数
 * 这里需要注意,回传的消息必须是广播.因为客户端和服务端都不知道对方的唯一标志,所以只能通过广播的方式来回传消息
 * 客户端接收到广播消息后,需要自行判断是否是自己需要的消息,然后进行业务操作,或者丢弃
 *
 * 具体到API,创建P2P信道时,需要传递一个onMessage的回调函数,用于接收服务端或者客户端返回的消息
 * 那么 问题来了,服务端接收到创建P2P信道的消息后,如何创建唯一的订阅者呢?
 *
 * client.send("/system/createP2PChannel", (message) -> {
 *    // 这里需要判断是否是自己需要的消息
 *    // 如果是,则进行业务操作
 *    // 如果不是,则丢弃
 *    });
 */
public class P2PMessageExchangeStrategy extends AbstractMessageExchangeStrategy{

    public P2PMessageExchangeStrategy(IMessageSubscriberManager subscriberManager) {
        super(subscriberManager);
    }

    @Override
    public void doExchange(Message<?> message) {

    }
}
