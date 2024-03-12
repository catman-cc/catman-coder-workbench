package cc.catman.coder.workbench.core.message.subscriber;

import cc.catman.coder.workbench.core.message.exchange.IMessageExchange;

public interface PostExchangeInjectMessageSubscriber extends IMessageSubscriber{

    void injectMessageExchange(IMessageExchange exchange);
}
