package cc.catman.workbench.api.server.configuration.message.subscriber;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageACK;
import cc.catman.coder.workbench.core.message.MessageResult;
import cc.catman.coder.workbench.core.message.subscriber.IMessageSubscriber;
import jakarta.ws.rs.core.GenericType;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractAntPathMessageSubscriber<T> extends GenericType<T> implements IMessageSubscriber {

    protected AntPathMatcher matcher = new AntPathMatcher();
    protected List<String> variableNames=new ArrayList<>();
    protected List<String> optionalVariableNames=new ArrayList<>();

    protected String getPrefix(){
        return "";
    }

    protected  String getPattern(){
        return buildPattern();
    }


    @Override
    public boolean isMatch(Message<?> message) {
        return matcher.match(getPattern(), message.getTopic());
    }

    @SuppressWarnings("unchecked")
    @Override
    public MessageResult onReceive(Message<?> message) {
        Map<String, String> variables = matcher.extractUriTemplateVariables(getPattern(), message.getTopic());
        if (!checkVariables(variables)){
            // 变量不匹配
            return handleError(message, new IllegalArgumentException("变量不匹配"));
        }
        Message<T> payload = (Message<T>) message.covert(getRawType());
        return doMessage(payload, variables);
    }

    protected boolean checkVariables(Map<String, String> variables){
       return variableNames.stream().allMatch(v-> variables.containsKey(v) || optionalVariableNames.contains(v));
    }

    protected abstract MessageResult doMessage(Message<T> message, Map<String,String> variables);

    protected MessageResult handleError(Message<?> message, Exception e){
        return MessageResult.builder().ack(MessageACK.DROP).doNext(false).build();
    }

    protected String buildPattern(){
        return buildPattern(getPrefix(), "/", variableNames.toArray(new String[0]));
    }

    protected String buildPattern(String prefix,String separator, String... parts){
        StringBuilder builder = new StringBuilder(prefix);
        if (parts.length==0){
            return builder.toString();
        }
        for (String part : parts){
            builder.append("{").append(part).append("}").append(separator);
        }
        return builder.substring(0, builder.length()-1);

    }

}
