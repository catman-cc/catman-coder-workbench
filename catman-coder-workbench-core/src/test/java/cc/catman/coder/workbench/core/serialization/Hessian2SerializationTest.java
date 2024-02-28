package cc.catman.coder.workbench.core.serialization;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Hessian2SerializationTest {

    @Test
    void serialize() {
        Hessian2Serialization serialization=new Hessian2Serialization();
        byte[] serialize = serialization.serialize(circle());
        System.out.println(serialize.length);
        System.out.println(serialization.deserialize(serialize));
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