package cc.catman.coder.workbench.core.message;

/**
 * 消息订阅者
 */
public interface MessageSubscriber<T> extends MessageHandler<T> {
   default boolean subscribe(MessageExchange exchange){
       exchange.subscribe(this);
         return true;
   }



    MessageMatch getMessageMatch();
}
