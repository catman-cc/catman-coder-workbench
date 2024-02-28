package cc.catman.coder.workbench.core.message.match;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.AntPathMatcher;

/**
 * AntPath消息匹配器
 */
@Data
@Builder
public class AntPathMessageMatch implements MessageMatch {
    private String pattern;
    private boolean fullMatch;

    @Builder.Default
    protected AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static AntPathMessageMatch of(String pattern, boolean fullMatch) {
        return AntPathMessageMatch.builder().pattern(pattern).fullMatch(fullMatch).build();
    }

    public static AntPathMessageMatch of(String pattern) {
        return AntPathMessageMatch.builder().pattern(pattern).fullMatch(false).build();
    }

    @Override
    public boolean match(Message<?> message) {
        return this.fullMatch ? antPathMatcher.match(this.pattern, message.getTopic()) : antPathMatcher.matchStart(this.pattern, message.getTopic());
    }
}
