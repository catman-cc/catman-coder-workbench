package cc.catman.workbench.api.server.configuration.message.exchange;

import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.exchange.DefaultMessageExchange;
import cc.catman.coder.workbench.core.message.match.AntPathMessageMatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MessageExchangeConfiguration {

    @Bean
    public DefaultMessageExchange exchange() {
        DefaultMessageExchange exchange=new DefaultMessageExchange();

        exchange.add((message -> {
            log.info("netty message:{}",message);
        }));

        // 注册节点接入的消息交换策略
        exchange.add(AntPathMessageMatch.of("catman.cc/core/node/**"),(message -> {
            // 处理节点接入信息,这里只是打印消息
            log.info("node-join message:{}",message);
            return new MessageResult();
        }));
        return exchange;
    }
}
