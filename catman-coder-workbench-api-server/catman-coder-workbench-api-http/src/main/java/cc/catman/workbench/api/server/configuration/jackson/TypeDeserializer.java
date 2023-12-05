package cc.catman.workbench.api.server.configuration.jackson;

import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.complex.ComplexType;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 针对类型,提供特殊的序列化,主要目的是支持子类
 */
public class TypeDeserializer extends JsonDeserializer<DefaultType> {
    private Map<String,Class<? extends DefaultType>> maps=new HashMap<>();

    public TypeDeserializer add(String type,Class<? extends DefaultType> clazz){
        this.maps.put(type,clazz);
        return this;
    }
    @Override
    public DefaultType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        TreeNode treeNode = p.readValueAsTree();
        String type = treeNode.get("typeName").traverse(p.getCodec()).readValueAs(String.class);
        Class<? extends DefaultType> clazz = maps.getOrDefault(type, ComplexType.class);
        try (JsonParser traverse = treeNode.traverse(p.getCodec());){
           return traverse.readValueAs(clazz);
       }
    }
}
