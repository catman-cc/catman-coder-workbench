package cc.catman.coder.workbench.core.serialization;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FastJson2SerializationTest {

    @Test
    void serialize() {
        FastJson2Serialization serialization=new FastJson2Serialization();
        byte[] serialize = serialization.serialize(circle());
        System.out.println(new String(serialize));
        Object dObj = serialization.deserialize(serialize);
        System.out.println(dObj);
    }

    @Test
    void deserialize() {
    }
    Object circle(){
        Map<String,Object> a=new HashMap<>();
        Map<String,Object> b=new HashMap<>();
        a.put("b",b);
        b.put("a",a);
        return a;
    }
}