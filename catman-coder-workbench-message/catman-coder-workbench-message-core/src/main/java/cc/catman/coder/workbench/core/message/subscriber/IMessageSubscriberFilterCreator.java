package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;


/**
 * 消息订阅者过滤器工厂,用于创建消息订阅者过滤器
 */
public interface IMessageSubscriberFilterCreator {
   default boolean supports(Message<?> message){
       return true;
   }

    IMessageSubscriberFilter createFilters(Message<?> message);
}
