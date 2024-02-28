package cc.catman.coder.workbench.core.message.message;

import cc.catman.coder.workbench.core.message.Message;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MessagePayloadDeserializer extends JsonDeserializer<Message<?>>{

    @Override
    public Message<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return null;
    }
}
