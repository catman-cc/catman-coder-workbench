package cc.catman.coder.workbench.core.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

public class ObjectMapperMessageDecoder implements IMessageDecoder {
    private ObjectMapper objectMapper;

    public ObjectMapperMessageDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getPayload() instanceof ObjectNode;
    }

    @Override
    @SneakyThrows
    public <T> Message<T> decode(Message<?> message, Class<T> clazz) {
        ObjectNode payload = (ObjectNode) message.getPayload();
        T t = objectMapper.readValue(payload.toString(), clazz);
        return message.copy(t);
    }
}
