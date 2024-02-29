package cc.catman.coder.workbench.core.message;

import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

@Slf4j
public class MessageExchangeConfiguration {

    @Bean
    public DefaultMessageExchange defaultMessageExchange() {
        DefaultMessageExchange messageExchange = new DefaultMessageExchange();
        messageExchange.add(message -> {
            log.info("Received message: {}", message);
            return true;
        });
        return messageExchange;
    }
}
