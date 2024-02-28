package cc.catman.coder.workbench.core.message.match;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageMatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EqualsTopicMessageMatch implements MessageMatch {
    private String topic;
    public static EqualsTopicMessageMatch of(String topic) {
        EqualsTopicMessageMatch equalsTopicMessageMatch = new EqualsTopicMessageMatch();
        equalsTopicMessageMatch.setTopic(topic);
        return equalsTopicMessageMatch;
    }
    @Override
    public boolean match(Message<?> message) {
        return this.topic.equals(message.getTopic());
    }
}
