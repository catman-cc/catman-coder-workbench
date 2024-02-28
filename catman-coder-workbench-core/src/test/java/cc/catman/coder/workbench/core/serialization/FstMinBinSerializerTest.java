package cc.catman.coder.workbench.core.serialization;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FstMinBinSerializerTest {

    @Test
    void serialize() {
        FstMinBinSerializer fstMinBinSerializer = new FstMinBinSerializer();
        Object obj = circle();
        byte[] bytes = fstMinBinSerializer.serialize(obj);
        for (byte b : bytes) {
            System.out.print(b);
        }
        System.out.println();
        System.out.println(new String(bytes));
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    void deserialize() {
        FstMinBinSerializer fstMinBinSerializer = new FstMinBinSerializer();
        Object obj = circle();
        byte[] bytes = fstMinBinSerializer.serialize(obj);
        Object deserialized = fstMinBinSerializer.deserialize(bytes);
        assertNotNull(deserialized);
        assert deserialized instanceof Map<?,?>;
        Map<?,?> map = (Map<?,?>) deserialized;
        assert map.containsKey("b");
        Object bMap = map.get("b");
        assert bMap instanceof Map<?,?>;
        assert ((Map<?, ?>) bMap).containsKey("a");
        assert map.equals(((Map<?, ?>) bMap).get("a"));
    }

    Object circle(){
        Map<String,Object> a=new HashMap<>();
        Map<String,Object> b=new HashMap<>();
        a.put("b",b);
        b.put("a",a);
        return a;
    }
}