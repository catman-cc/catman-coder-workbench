package cc.catman.workbench.api.server.configuration.jackson;

import cc.catman.coder.workbench.core.value.providers.DefaultValueProvider;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ValueProviderDeserializer extends JsonDeserializer<DefaultValueProvider> {
    private Map<String,Class<? extends DefaultValueProvider>> maps=new HashMap<>();

    public JsonDeserializer<DefaultValueProvider> add(String type, Class<? extends DefaultValueProvider> clazz){
        this.maps.put(type,clazz);
        return this;
    }
    @Override
    public DefaultValueProvider deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return null;
    }
}
