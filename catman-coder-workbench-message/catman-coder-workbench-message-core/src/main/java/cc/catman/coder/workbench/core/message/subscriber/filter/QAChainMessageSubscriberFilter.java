package cc.catman.coder.workbench.core.message.subscriber.filter;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;

import java.util.List;

/**
 *  问答链,消息订阅者过滤器,用于过滤消息订阅者,消息应答模式下,理论上会换位固定的topic
 */
public class QAChainMessageSubscriberFilter extends AbstractMessageSubscriberFilter {
    public static final String QA_CHAIN_ID = "system:qa:chain-id";

    protected String qaChainId;
    public QAChainMessageSubscriberFilter(Message<?> message) {
        super(message);
        this.qaChainId = message.getAttr(QA_CHAIN_ID);
    }

    @Override
    public List<IMessageSubscriber> filter(List<IMessageSubscriber> subscribers) {
        // 此时就要求,消息订阅者中包含统一的应答链链路ID
        return subscribers.stream()
                .filter(subscriber -> hasAttrs(subscriber, QA_CHAIN_ID))
                .filter(subscriber -> subscriber.getAttr(QA_CHAIN_ID).equals(qaChainId))
                .toList();
    }
}
