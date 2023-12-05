package cc.catman.workbench.api.server.configuration.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UniversalJsonDeserializer<T> extends JsonDeserializer<T> {
    private String key;
    private Class<T> defaultClass;

    public UniversalJsonDeserializer(String key, Class<T> defaultClass) {
        this.key = key;
        this.defaultClass = defaultClass;
    }

    private Map<String,Class<? extends T>> maps=new HashMap<>();

    public  UniversalJsonDeserializer<T> add(String type,Class<? extends T> clazz){
        this.maps.put(type,clazz);
        return this;
    }
    @Override
    public T deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        TreeNode treeNode = p.readValueAsTree();
        String type = treeNode.get(key).traverse(p.getCodec()).readValueAs(String.class);
        Class<? extends T> clazz = maps.getOrDefault(type, defaultClass);
        try (JsonParser traverse = treeNode.traverse(p.getCodec());){
            return traverse.readValueAs(clazz);
        }
    }
}
